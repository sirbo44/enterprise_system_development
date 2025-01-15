package com.example.library_webapplication;

import com.example.library_webapplication.AppScopeBean;
import com.example.library_webapplication.entities.Book;
import com.example.library_webapplication.entities.JkBooksUser;
import com.example.library_webapplication.entities.User;
import com.example.library_webapplication.repos.UserRepository;
import com.example.library_webapplication.services.MainService;
import com.example.library_webapplication.repos.BookRepository;
import com.example.library_webapplication.repos.JkBooksUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class MainController {
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Autowired
    MainService mainService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JkBooksUserRepository jkBooksUserRepository;
    @Autowired
    private AppScopeBean applicationScopeBean;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String dologin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session) {
        User user = mainService.dologin(username, password);
        String next_page = "login";
        if (user!=null) {
            next_page = switch (user.getRole()) {
                case "administrator" -> "redirect:/adminPage";
                case "librarian" -> "redirect:/librarianPage";
                default -> "redirect:/homePage";
            };
            applicationScopeBean.setNumberofusers(applicationScopeBean.getNumberofusers() + 1);
            session.setAttribute("loggedUser", user);
        }
        return next_page;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/doregister")
    public String doregister(@RequestParam String username,
                             @RequestParam String password1,
                             @RequestParam String password2) {
        String next_page = "/login";
        if (!password1.equals(password2)) {
            next_page = "/register";
            String error_message = "not matching passwords";
        } else{
            User user = mainService.uniqueUsername(username);
            if (user != null){
                next_page = "/register";
                String error_message = "existing user";
            } else {
                //add the new user and add it into the database
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password1);
//                newUser.setPassword(passwordEncoder.encode(password1));
                newUser.setRole("member");
                userRepository.save(newUser);
            }
        }
        return next_page;
    }

    @GetMapping("adminPage")
    public String displayAdmin(ModelMap model,
                               HttpSession session) {
        Integer checkedInBooks = (int) (long) mainService.findAllCheckedInBooks();
        Integer checkedOutBooks = mainService.findAllCheckedOutBooks();
        List<List<Object>> mylist =List.of(
                                    List.of("No", checkedInBooks),
                                    List.of("Yes", checkedOutBooks)
        );
        List<JkBooksUser> bookList = mainService.findAllJkBooksUser();
        model.addAttribute("books", mylist);
        model.addAttribute("LoggedUser", session.getAttribute("loggedUser"));
        model.addAttribute("bookList", bookList);
        return"adminPage";
    }

    @GetMapping("userManagement")
    public String showUsers(ModelMap model) {
        List<User> users = userRepository.findAll();
        List<String> roles = Arrays.asList("administrator","librarian","member");
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "userManagement";
    }

    @PostMapping("cancelAdmin")
    public String cancelAdminForm() {
        return "redirect:/adminPage";
    }

    @PostMapping("saveUserChanges")
    public String saveAdminForm(@RequestParam(name = "username") String username,
                                @RequestParam(name = "role") String role) {
        User user = mainService.uniqueUsername(username);
        user.setRole(role);
        userRepository.save(user);
        System.out.println(username + " : " + role);
        return "redirect:userManagement";
    }

    @GetMapping("userBooks")
    public String showUserBooks(@RequestParam String username,
                                ModelMap model) {
        User user = mainService.uniqueUsername(username);
//        List<Book> books = mainService.findByUsername(user);
//        model.addAttribute("books", books);
        return "userBooks";
    }
    @GetMapping("test")
    public String test() {
        return "redirect:/userBooks";
    }





    @GetMapping("librarianPage")
    public String displayLibrarian(ModelMap model,
                                   HttpSession session) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "librarianPage";
    }

    @PostMapping("librarianPage")
    public String searchLibBar(ModelMap model,
                            @RequestParam String search,
                            HttpSession session) {
        List<Object> books = mainService.searchList(search, search);
        if (books.get(0) == null){
            model.addAttribute("books", books.get(1));
        } else {
            model.addAttribute("books", books.get(0));
        }
        return "librarianPage";
    }

    @GetMapping("delRecord")
    public String deleteRecord() {
        return "librarianPage";
    }

    @PostMapping("delRecord")
    public String deleteRecord(@RequestParam(name = "isbn") String isbn) {
        bookRepository.deleteById(isbn);
        return "redirect:/librarianPage";
    }

    @GetMapping("newRecord")
    public String addForm() {
        return "newRecord";
    }

    @PostMapping("addRecord")
    public String addRec(@RequestParam(name = "title") String title,
                         @RequestParam(name = "isbn") String isbn,
                         @RequestParam(name = "language") String language,
                         @RequestParam(name = "release") LocalDate release,
                         @RequestParam(name = "summary") String summary,
                         @RequestParam(name = "pieces") Integer pieces) {
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setLanguage(language);
        book.setRelease(release);
        book.setSummary(summary);
        book.setPieces(pieces);
        bookRepository.save(book);
        return "redirect:/librarianPage";
    }

    @GetMapping("editRecord")
    public String showEditPage(@RequestParam String isbn,
                               ModelMap model) {
        Book book = bookRepository.findByIsbn(isbn);
        model.addAttribute("book", book);
        return "editPage";
    }

    @PostMapping("editRecord")
    public String editForm(@RequestParam(name = "title") String title,
                           @RequestParam(name = "isbn") String isbn,
                           @RequestParam(name = "language") String language,
                           @RequestParam(name = "release") LocalDate release,
                           @RequestParam(name = "summary") String summary,
                           @RequestParam(name = "pieces") Integer pieces) {
        Book existingBook = bookRepository.findByIsbn(isbn);
        existingBook.setTitle(title);
        existingBook.setLanguage(language);
        existingBook.setRelease(release);
        existingBook.setSummary(summary);
        existingBook.setPieces(pieces);
        bookRepository.save(existingBook);

        return "redirect:/librarianPage";
    }

    @PostMapping("cancelLib")
    public String cancelLibForm() {
        return "redirect:/librarianPage";
    }

    @GetMapping("homePage")
    public String displayHome(ModelMap model,
                              HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        List<JkBooksUser> bookedList = mainService.findAllByUsername(user.getUsername());
        List<String> bookedListString = new ArrayList<>();
        for (JkBooksUser booked: bookedList){
            bookedListString.add(booked.getIsbn().getIsbn());
        }
        List<Book> books = bookRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("books", books);
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        model.addAttribute("bookedList", bookedListString);
        System.out.println(bookedListString);
        return "homePage";
    }

    @PostMapping("homePage")
    public String searchHomeBar(ModelMap model,
                                @RequestParam String search,
                                HttpSession session) {
        List<Object> books = mainService.searchList(search, search);
        if (books.get(0) == null){
            model.addAttribute("books", books.get(1));
        } else {
            model.addAttribute("books", books.get(0));
        }
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "homePage";
    }

    @PostMapping("checkinBook")
    public String checkBook(@RequestParam(name = "isbn") Book book,
                            HttpSession session,
                            ModelMap model) {
        JkBooksUser booksUser = new JkBooksUser();
        booksUser.setIsbn(book);
        booksUser.setUsername((User) session.getAttribute("loggedUser"));
        jkBooksUserRepository.save(booksUser);
        book.setPieces(book.getPieces() - 1);
        bookRepository.save(book);
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "redirect:/homePage";
    }

    @PostMapping("checkoutBook")
    public String checkoutBook(@RequestParam(name = "isbn") Book currentbook,
                               HttpSession session,
                               ModelMap model){
        JkBooksUser bookRecord = mainService.findByUsernameAndIsbn((User) session.getAttribute("loggedUser"),currentbook);
        jkBooksUserRepository.deleteById(bookRecord.getId());
        String isbn = currentbook.getIsbn();
        Book book = mainService.findByIsbn(isbn);
        book.setPieces(book.getPieces() + 1);
        bookRepository.save(book);
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "redirect:/homePage";
    }

    @PostMapping("destroy")
    public String destroySession(HttpServletRequest request){
        request.getSession().invalidate();
        applicationScopeBean.setNumberofusers(applicationScopeBean.getNumberofusers() - 1);
        return "redirect:/";
    }

    @GetMapping("book/{isbn}")
    public String showBook(@PathVariable String isbn,
                           ModelMap model) {
        Book book = mainService.findByIsbn(isbn);
        model.addAttribute("book", book);
        System.out.println(isbn);
        return "book";
    }

    @GetMapping("checkList")
    public String showCheckList() {
        List<JkBooksUser> jkBooksUser = mainService.findAllJkBooksUser();
        System.out.println(jkBooksUser);
        return "checkList";
    }
    @GetMapping("mybooks")
    public String showMyBooks(HttpSession session,
                              ModelMap model) {
        User user = (User) session.getAttribute("loggedUser");
        List<JkBooksUser> bookedList = mainService.findAllByUsername(user.getUsername());
        System.out.println(user.getUsername());
        System.out.println(bookedList);
        model.addAttribute("bookedList", bookedList);
        model.addAttribute("loggedUser", user);
        return "mybooks";
    }

    @PostMapping("cancelNew")
    public String cancelNewForm() {
        return "redirect:/librarianPage";
    }
}
