package com.example.library_webapplication;

import com.example.library_webapplication.entities.*;
import com.example.library_webapplication.repos.*;
import com.example.library_webapplication.services.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private JkBooksAuthorRepository jkBooksAuthorRepository;

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
                             @RequestParam String password2,
                             ModelMap model) {
        String next_page = "/login";
        if (!password1.equals(password2)) {
            next_page = "/register";
            String error_message = "not matching passwords";
        } else{
            User user = mainService.uniqueUsername(username);
            if (user != null){
                next_page = "/register";
                String error_message = "existing user";
                model.addAttribute("errorMessage", error_message);
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
        return "redirect:userManagement";
    }

    @GetMapping("userBooks")
    public String showUserBooks(@RequestParam String username,
                                ModelMap model) {
        User user = mainService.uniqueUsername(username);
        List<JkBooksUser> jkBooksUsersbooks = mainService.findAllByUsername(user.getUsername());
        List<JkBooksAuthor> booksAuthors = jkBooksAuthorRepository.findAll();
        List<Book> books = new ArrayList<>();
        for (JkBooksUser jkBooksUser: jkBooksUsersbooks){
            books.add(jkBooksUser.getIsbn());
        }

        model.addAttribute("books", books);
        model.addAttribute("booksAuthors", booksAuthors);
        return "userBooks";
    }

    @GetMapping("librarianPage")
    public String displayLibrarian(ModelMap model,
                                   HttpSession session) {
        List<Book> books = bookRepository.findAll();
        List<Author> authors = authorRepository.findAll();
        List<JkBooksAuthor> booksAuthors = jkBooksAuthorRepository.findAll();
        model.addAttribute("books", books);
        model.addAttribute("authors", authors);
        model.addAttribute("booksAuthors", booksAuthors);
        return "librarianPage";
    }

    @PostMapping("librarianPage")
    public String searchLibBar(ModelMap model,
                            @RequestParam String search,
                            HttpSession session) {
        List<Book> books = mainService.searchList(search, search);
        model.addAttribute("books", books);
        return "librarianPage";
    }

    @GetMapping("delRecord")
    public String deleteRecord() {
        return "librarianPage";
    }

    @PostMapping("delRecord")
    public String deleteRecord(@RequestParam(name = "isbn") String isbn) throws IOException {
        bookRepository.deleteById(isbn);
        Files.deleteIfExists(Path.of(System.getProperty("user.dir") + "\\library_webApplication\\src\\main\\resources\\static\\img\\" + isbn +".jpg"));
        return "redirect:/librarianPage";
    }

    @GetMapping("newRecord")
    public String addForm(ModelMap model) {
        List<Author> authorList = mainService.findAllAuthors();
        model.addAttribute("authorList", authorList);
        return "newRecord";
    }

    @PostMapping("addRecord")
    public String addRec(@RequestParam(name = "title") String title,
                         @RequestParam(name = "isbn") String isbn,
                         @RequestParam(name = "language") String language,
                         @RequestParam(name = "release") LocalDate release,
                         @RequestParam(name = "summary") String summary,
                         @RequestParam(name = "pieces") Integer pieces,
                         @RequestParam(name = "author") Integer author,
                         @RequestParam(name = "image") MultipartFile file) throws IOException {
        Path fileNameAndPath = Paths.get(System.getProperty("user.dir") + "\\library_webApplication\\src\\main\\resources\\static\\img\\", isbn+".jpg");
        Files.write(fileNameAndPath, file.getBytes());
        Book book = new Book();
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setLanguage(language);
        book.setRelease(release);
        book.setSummary(summary);
        book.setPieces(pieces);
        book.setPhoto(isbn+".jpg");
        bookRepository.save(book);
        JkBooksAuthor bookAuthor = new JkBooksAuthor();
        bookAuthor.setAuthor(mainService.findAuthorById(author));
        bookAuthor.setBookCode(book);
        jkBooksAuthorRepository.save(bookAuthor);
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

    @GetMapping("newAuthor")
    public String showAuthorForm() {
        return "newAuthor";
    }

    @PostMapping("newAuthor")
    public String addRec(@RequestParam(name = "name") String name,
                         @RequestParam(name = "dob") LocalDate dob,
                         @RequestParam(name = "nationality") String nationality) {
        Author author = new Author();
        author.setName(name);
        author.setDob(dob);
        author.setNationality(nationality);
        authorRepository.save(author);
        return "redirect:/librarianPage";
    }

    @GetMapping("authors")
    public String showAuthors(ModelMap model) {
        List<Author> authorList = mainService.findAllAuthors();
        model.addAttribute("authorList", authorList);
        return "authors";
    }

    @PostMapping("delAuthor")
    public String deleteAuthor(@RequestParam(name = "id") Integer id) {
        Author author = mainService.findAuthorById(id);
        authorRepository.delete(author);
        return "redirect:/authors";
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
        return "homePage";
    }

    @PostMapping("homePage")
    public String searchHomeBar(ModelMap model,
                                @RequestParam String search,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        List<JkBooksUser> bookedList = mainService.findAllByUsername(user.getUsername());
        List<Book> books = mainService.searchList(search, search);
        List<String> bookedListString = new ArrayList<>();
        for (JkBooksUser booked: bookedList){
            bookedListString.add(booked.getIsbn().getIsbn());
        }
        model.addAttribute("books", books);
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        model.addAttribute("bookedList", bookedListString);
        return "homePage";
    }

    @PostMapping("checkinBook")
    public String checkBook(@RequestParam(name = "isbn") Book book,
                            HttpSession session,
                            ModelMap model) {
        JkBooksUser booksUser = new JkBooksUser();
        booksUser.setIsbn(book);
        booksUser.setUsername((User) session.getAttribute("loggedUser"));
        booksUser.setCheckIn(LocalDate.now());
        booksUser.setCheckOut(LocalDate.now().plusDays(7));
        // pop up to inform the user about the checkout
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
                           ModelMap model,
                           HttpSession session) {
        Book book = mainService.findByIsbn(isbn);
        model.addAttribute("book", book);
        model.addAttribute("loggedUser", session.getAttribute("loggedUser"));
        return "book";
    }

    @GetMapping("checkList")
    public String showCheckList(ModelMap model) {
        List<JkBooksUser> bookedList = mainService.findAllJkBooksUser();
        model.addAttribute("bookedList", bookedList);
        return "checkList";
    }

    @GetMapping("mybooks")
    public String showMyBooks(HttpSession session,
                              ModelMap model) {
        User user = (User) session.getAttribute("loggedUser");
        List<JkBooksUser> bookedList = mainService.findAllByUsername(user.getUsername());
        model.addAttribute("bookedList", bookedList);
        model.addAttribute("loggedUser", user);
        return "mybooks";
    }

    @PostMapping("cancelNew")
    public String cancelNewForm() {
        return "redirect:/librarianPage";
    }
}

