package com.example.library_webapplication.services;

import com.example.library_webapplication.entities.*;
import com.example.library_webapplication.repos.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;
import java.util.List;

@Service
public class MainService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JkBooksUserRepository jkBooksUserRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private JkBooksAuthorRepository jkBooksAuthorRepository;

    public User dologin(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User uniqueUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<Book> allBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchList(String isbn, String title) {
        List<Book> mylist = new ArrayList<>();
        if (!bookRepository.findByIsbnContaining(isbn).isEmpty()) {
            mylist.addAll(bookRepository.findByIsbnContaining(isbn));
        }
        if (!bookRepository.findByTitleContainingIgnoreCase(title).isEmpty()) {
            mylist.addAll(bookRepository.findByTitleContainingIgnoreCase(title));
        }
        return mylist;
    }

    public Book deleteBooksByIsbn(String isbn){
        return bookRepository.deleteBooksByIsbn(isbn);
    }

    public JkBooksUser findByUsername(User username) {
        return jkBooksUserRepository.findByUsername(username);
    }

    public List<JkBooksUser> findAllByUsername(String username){
        return (List<JkBooksUser>) jkBooksUserRepository.findAllByUsername(uniqueUsername(username));
    }

    public JkBooksUser findByUsernameAndIsbn(User username, Book isbn){
        return jkBooksUserRepository.findByUsernameAndIsbn(username, isbn);
    }

    public Book findByIsbn(String isbn){
        return bookRepository.findByIsbn(isbn);
    }

    public List<JkBooksUser> findAllJkBooksUser() {
        return (List<JkBooksUser>) jkBooksUserRepository.findAll();
    }

    public Long findAllCheckedInBooks() {
        return jkBooksUserRepository.count();
    }

    public Integer findAllCheckedOutBooks() {
        List<Book> books = bookRepository.findAll();
        int sum = 0;
        for (Book book: books){
            sum = sum + book.getPieces();
        }
        return sum;
    }

    public List<Author> findAllAuthors(){
        return authorRepository.findAll();
    }

    public Author findAuthorById(Integer id) {
        return authorRepository.findAuthorById(id);
    }

    public ByteArrayOutputStream createFile() throws DocumentException, IOException {
        List<User> users = userRepository.findAll();
        List<Book> books = bookRepository.findAll();
        List<Author> authors = authorRepository.findAll();
        List<JkBooksUser> jkBooksUsers = jkBooksUserRepository.findAll();
        List<JkBooksAuthor> jkBooksAuthors = jkBooksAuthorRepository.findAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        // file's title = current date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        document.addTitle(formatter.format(date));
        PdfPTable firstRowTable = new PdfPTable(2);
        firstRowTable.setWidthPercentage(85);
        firstRowTable.setWidths(new int[]{1, 2});
        // first row
        Image img = Image.getInstance(System.getProperty("user.dir") + "\\library_webApplication\\src\\main\\resources\\static\\img\\logo.jpg");
        img.scaleToFit(100, 100);
        PdfPCell imgCell = new PdfPCell(img, false);
        imgCell.setBorder(Rectangle.NO_BORDER);
        firstRowTable.addCell(imgCell);
        PdfPCell textCell = new PdfPCell();
        textCell.addElement(new Paragraph("Name: Admin"));
        textCell.addElement(new Paragraph("Date: " + formatter.format(date)));
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setPaddingLeft(160);
        firstRowTable.addCell(textCell);
        document.add(firstRowTable);
        // second row       CHANGE TO GET THE CORRECT PIE CHART
        PdfPTable secondRowTable = new PdfPTable(1);
        secondRowTable.setWidthPercentage(85);
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Returned", findAllCheckedOutBooks());
        dataset.setValue("Not returned", (int) (long) findAllCheckedInBooks());
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Category Distribution",
                dataset,
                true,
                true,
                false
        );
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setCircular(true);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ({2})")); // Label format: Type, Count, Percentage
        int chartWidth = 500;
        int chartHeight = 300;
        BufferedImage bufferedImage = pieChart.createBufferedImage(chartWidth, chartHeight);
        ByteArrayOutputStream chartOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", chartOutputStream);
        byte[] chartBytes = chartOutputStream.toByteArray();
        Image chartImage = Image.getInstance(chartBytes);
        chartImage.scaleToFit(450, 300);
        PdfPCell chartCell = new PdfPCell(chartImage, false);
        chartCell.setBorder(Rectangle.NO_BORDER);
        secondRowTable.addCell(chartCell);
        document.add(secondRowTable);

        // third row
        Paragraph thirdParagrath = new Paragraph("Users List");
        thirdParagrath.setPaddingTop(20);
        document.add(thirdParagrath);
        PdfPTable thirdRowTable = new PdfPTable(3);
        thirdRowTable.setPaddingTop(20);
        thirdRowTable.setWidthPercentage(80);
        thirdRowTable.addCell("Username");
        thirdRowTable.addCell("Role");
        thirdRowTable.addCell("Books");
        for (User user: users) {
            Integer bookNum = jkBooksUserRepository.countByUsername(user);
            thirdRowTable.addCell(user.getUsername());
            thirdRowTable.addCell(user.getRole());
            thirdRowTable.addCell(String.valueOf(bookNum));
        }
        document.add(thirdRowTable);
        // fourth row
        Paragraph fourthParagraph = new Paragraph("Users with CheckedIn Books");
        fourthParagraph.setPaddingTop(20);
        document.add(fourthParagraph);
        for (User user: users) {
            com.itextpdf.text.List bookList = new com.itextpdf.text.List();
            for (JkBooksUser jkBookUser: jkBooksUsers){
                if (user.getUsername() == jkBookUser.getUsername().getUsername()) {
                    bookList.add(jkBookUser.getIsbn().getTitle());
                }
            }
            if (!bookList.isEmpty()) {
                Paragraph userName = new Paragraph(user.getUsername());
                userName.setIndentationLeft(120);
                bookList.setIndentationLeft(140);
                document.add(userName);
                document.add(bookList);
            }
        }

        // fifth row
        Paragraph fifthParagraph = new Paragraph("Books List");
        fifthParagraph.setPaddingTop(20);
        document.add(fifthParagraph);
        PdfPTable fifthRowTable = new PdfPTable(6);
        fifthRowTable.setPaddingTop(20);
        fifthRowTable.setWidthPercentage(85);
        for (Book book: books) {
            fifthRowTable.addCell(book.getTitle());
            fifthRowTable.addCell(book.getIsbn());
            fifthRowTable.addCell(book.getLanguage());
            fifthRowTable.addCell(book.getPieces().toString());
            if (book.getPhoto()==null) {
                fifthRowTable.addCell("No");
            }else {
                fifthRowTable.addCell("Yes");
            }
            StringJoiner stringjoiner = new StringJoiner(",");
            for (JkBooksAuthor author: jkBooksAuthors){
                if (author.getBookCode().getIsbn() == book.getIsbn()){
                    stringjoiner.add(author.getAuthor().getName());
                }
            }
            fifthRowTable.addCell(stringjoiner.toString());
        }
        document.add(fifthRowTable);
        // sixth row
        Paragraph sixthParagrath = new Paragraph("Authors List");
        document.add(sixthParagrath);
        sixthParagrath.setPaddingTop(20);
        PdfPTable sixthRowTable = new PdfPTable(3);
        sixthRowTable.setWidthPercentage(80);
        sixthRowTable.setPaddingTop(20);
        sixthRowTable.addCell("Name");
        sixthRowTable.addCell("Date of Birth");
        sixthRowTable.addCell("Nationality");
        for (Author author: authors) {
            sixthRowTable.addCell(author.getName());
            sixthRowTable.addCell(author.getDob().toString());
            sixthRowTable.addCell(author.getNationality());
        }
        document.add(sixthRowTable);
        // seventh row
        Paragraph seventhParagrath = new Paragraph("Count List");
        seventhParagrath.setPaddingTop(20);
        document.add(seventhParagrath);
        PdfPTable seventhRowTable = new PdfPTable(2);
        seventhRowTable.setWidthPercentage(80);
        seventhRowTable.setPaddingTop(20);
        seventhRowTable.addCell("Users");
        seventhRowTable.addCell(String.valueOf(userRepository.count()));
        seventhRowTable.addCell("Books");
        seventhRowTable.addCell(String.valueOf(bookRepository.count()));
        seventhRowTable.addCell("Author");
        seventhRowTable.addCell(String.valueOf(authorRepository.count()));
        document.add(seventhRowTable);
        // last row
        Paragraph copyright = new Paragraph("© " + Year.now(), FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC));
        copyright.setAlignment(Element.ALIGN_CENTER);
        document.add(copyright);
        document.close();
        return outputStream;
    }
}
