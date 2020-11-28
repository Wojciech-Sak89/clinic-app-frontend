package com.kodilla.clinic.learnvaadin.books.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BookService {

    private Set<Book> books;
    private static BookService bookService;

    private BookService() {
        this.books = exampleData();
    }

    public static BookService getInstance() {
        if (bookService == null) {
            bookService = new BookService();
        }
        return bookService;
    }

    public Set<Book> getBooks() {
        return new HashSet<>(books);
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public Set<Book> findByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().contains(title))
                .collect(Collectors.toSet());
    }

    public void save(Book book) {
        delete(book);
        Set<Book> updatedBooks = new HashSet<>(books);
        updatedBooks.add(book);
        this.books = updatedBooks;
    }

    public void delete(Book book) {
        if (books.contains(book)) System.out.println(book + " IS AVAILABLE TO DELETE!");
        if(this.books.remove(book)) System.out.println(book + " DELETED!");

        for (Book bookCollected : books) {
            System.out.println(bookCollected);
        }
    }

    private Set<Book> exampleData() {
        Set<Book> books = new HashSet<>();
        books.add(new Book("Cracking the Coding Interview: 189 Programming Questions and Solutions", "Gayle Laakmann McDowell",  "2015", BookType.IT));
        books.add(new Book("Introduction to Algorithms", "The MIT Press", "2009", BookType.IT));
        books.add(new Book("Introduction to the Theory of Computation", "Michael Sipser", "2012", BookType.IT));
        books.add(new Book("Operating System Concepts", "Silberschatz, Galvin, Gagne", "2012", BookType.IT));
        books.add(new Book("The Silent Patient", "Alex Michaelides", "2019", BookType.THRILLER));
        books.add(new Book("The Handmaid's Tale", "Margaret Atwood", "2019", BookType.CLASSIC));
        books.add(new Book("Watch Us Rise", "Renée Watson,  Ellen Hagan", "2019", BookType.POETRY));
        return books;
    }
}
