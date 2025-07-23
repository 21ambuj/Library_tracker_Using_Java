import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.AbstractTableModel;

class Book {
    String title;
    String author;
    boolean issued;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.issued = false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isIssued() {
        return issued;
    }

    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    @Override
    public String toString() {
        return title + " by " + author + (issued ? " (Issued)" : "");
    }
}

class Library {
    private static List<Book> books = new ArrayList<>();

    public static void addBook(String title, String author) {
        books.add(new Book(title, author));
    }

    public static void deleteBook(String title) {
        books.removeIf(book -> book.title.equalsIgnoreCase(title));
    }

    public static List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public static void issueBook(String title) {
        for (Book b : books) {
            if (b.title.equalsIgnoreCase(title) && !b.issued) {
                b.issued = true;
                break;
            }
        }
    }

    public static void returnBook(String title) {
        for (Book b : books) {
            if (b.title.equalsIgnoreCase(title) && b.issued) {
                b.issued = false;
                break;
            }
        }
    }

    public static List<Book> linearSearch(String query, String searchBy) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (searchBy.equalsIgnoreCase("title") && book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                results.add(book);
            } else if (searchBy.equalsIgnoreCase("author") && book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }

    public static Book binarySearch(String title) {
        List<Book> sortedBooks = new ArrayList<>(books);
        Collections.sort(sortedBooks, Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));

        int low = 0;
        int high = sortedBooks.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            Book midBook = sortedBooks.get(mid);
            int cmp = title.compareToIgnoreCase(midBook.getTitle());

            if (cmp == 0) {
                return midBook;
            } else if (cmp < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return null;
    }

    public static void sortByTitle() {
        Collections.sort(books, Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
    }

    public static void sortByAuthor() {
        Collections.sort(books, Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER));
    }
}

class BookTableModel extends AbstractTableModel {
    private List<Book> bookList;
    private final String[] columnNames = {"Title", "Author", "Status"};

    public BookTableModel(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return bookList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = bookList.get(rowIndex);
        switch (columnIndex) {
            case 0: return book.getTitle();
            case 1: return book.getAuthor();
            case 2: return book.isIssued() ? "Issued" : "Available";
            default: return null;
        }
    }
}

public class LibraryApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(RoleSelector::new);
    }
}

class RoleSelector extends JFrame {
    public RoleSelector() {
        setTitle("Library Book Tracker - Role Selection");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton adminBtn = new JButton("Continue as Admin");
        JButton studentBtn = new JButton("Continue as Student");

        adminBtn.setFont(new Font("Arial", Font.BOLD, 20));
        studentBtn.setFont(new Font("Arial", Font.BOLD, 20));

        adminBtn.addActionListener(e -> {
            dispose();
            new AdminPanel();
        });

        studentBtn.addActionListener(e -> {
            dispose();
            new StudentPanel();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(adminBtn, gbc);

        gbc.gridy = 1;
        panel.add(studentBtn, gbc);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}

class AdminPanel extends JFrame {
    private JTable bookTable;
    private BookTableModel tableModel;
    private JTextField searchField;

    public AdminPanel() {
        setTitle("Admin Dashboard - Library Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new BookTableModel(Library.getBooks());
        bookTable = new JTable(tableModel);
        bookTable.setFillsViewportHeight(true);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 16));
        bookTable.setRowHeight(24);
        bookTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton addBtn = new JButton("Add Book");
        JButton deleteBtn = new JButton("Delete Book");
        JButton issueBtn = new JButton("Issue Book");
        JButton returnBtn = new JButton("Return Book");
        JButton viewAllBtn = new JButton("View All Books");
        JButton sortByTitleBtn = new JButton("Sort by Title");
        JButton sortByAuthorBtn = new JButton("Sort by Author");
        JButton backBtn = new JButton("Back to Role Selection");

        searchField = new JTextField(20);
        JButton linearSearchBtn = new JButton("Linear Search (Title/Author)");
        JButton binarySearchBtn = new JButton("Binary Search (Title)");

        addBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        deleteBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        issueBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        returnBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        viewAllBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        sortByTitleBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        sortByAuthorBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        backBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        linearSearchBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        binarySearchBtn.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0; gbc.gridy = 0; controlPanel.add(addBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 0; controlPanel.add(deleteBtn, gbc);
        gbc.gridx = 2; gbc.gridy = 0; controlPanel.add(issueBtn, gbc);
        gbc.gridx = 3; gbc.gridy = 0; controlPanel.add(returnBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1; controlPanel.add(sortByTitleBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 1; controlPanel.add(sortByAuthorBtn, gbc);
        gbc.gridx = 2; gbc.gridy = 1; controlPanel.add(viewAllBtn, gbc);
        gbc.gridx = 3; gbc.gridy = 1; controlPanel.add(backBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; controlPanel.add(searchField, gbc);
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 1; controlPanel.add(linearSearchBtn, gbc);
        gbc.gridx = 3; gbc.gridy = 2; controlPanel.add(binarySearchBtn, gbc);

        addBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter book title:", "Add Book", JOptionPane.PLAIN_MESSAGE);
            if (title != null && !title.trim().isEmpty()) {
                String author = JOptionPane.showInputDialog(this, "Enter author name:", "Add Book", JOptionPane.PLAIN_MESSAGE);
                if (author != null && !author.trim().isEmpty()) {
                    Library.addBook(title.trim(), author.trim());
                    updateBookTable(Library.getBooks());
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter title to delete:", "Delete Book", JOptionPane.PLAIN_MESSAGE);
            if (title != null && !title.trim().isEmpty()) {
                Library.deleteBook(title.trim());
                updateBookTable(Library.getBooks());
            }
        });

        issueBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter title to issue:", "Issue Book", JOptionPane.PLAIN_MESSAGE);
            if (title != null && !title.trim().isEmpty()) {
                Library.issueBook(title.trim());
                updateBookTable(Library.getBooks());
            }
        });

        returnBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter title to return:", "Return Book", JOptionPane.PLAIN_MESSAGE);
            if (title != null && !title.trim().isEmpty()) {
                Library.returnBook(title.trim());
                updateBookTable(Library.getBooks());
            }
        });

        viewAllBtn.addActionListener(e -> updateBookTable(Library.getBooks()));

        sortByTitleBtn.addActionListener(e -> {
            Library.sortByTitle();
            updateBookTable(Library.getBooks());
        });

        sortByAuthorBtn.addActionListener(e -> {
            Library.sortByAuthor();
            updateBookTable(Library.getBooks());
        });

        linearSearchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                String[] options = {"Title", "Author"};
                int choice = JOptionPane.showOptionDialog(this, "Search by:", "Linear Search",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                List<Book> results = new ArrayList<>();
                if (choice == 0) { // Title
                    results = Library.linearSearch(query, "title");
                } else if (choice == 1) { // Author
                    results = Library.linearSearch(query, "author");
                }
                if (!results.isEmpty()) {
                    updateBookTable(results);
                } else {
                    JOptionPane.showMessageDialog(this, "No books found matching your search.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search query.", "Search Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        binarySearchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                Book result = Library.binarySearch(query);
                if (result != null) {
                    List<Book> results = new ArrayList<>();
                    results.add(result);
                    updateBookTable(results);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found using binary search (exact title match required).", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a title for binary search.", "Search Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new RoleSelector();
        });

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
        updateBookTable(Library.getBooks());
    }

    private void updateBookTable(List<Book> booksToDisplay) {
        tableModel.setBookList(booksToDisplay);
    }
}

class StudentPanel extends JFrame {
    private JTable bookTable;
    private BookTableModel tableModel;
    private JTextField searchField;

    public StudentPanel() {
        setTitle("Student Dashboard - Library Books");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new BookTableModel(Library.getBooks());
        bookTable = new JTable(tableModel);
        bookTable.setFillsViewportHeight(true);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 16));
        bookTable.setRowHeight(24);
        bookTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton issueBtn = new JButton("Issue Book");
        JButton returnBtn = new JButton("Return Book");
        JButton viewAllBtn = new JButton("View All Books");
        JButton sortByTitleBtn = new JButton("Sort by Title");
        JButton sortByAuthorBtn = new JButton("Sort by Author");
        JButton backBtn = new JButton("Back to Role Selection");

        searchField = new JTextField(20);
        JButton linearSearchBtn = new JButton("Linear Search (Title/Author)");
        JButton binarySearchBtn = new JButton("Binary Search (Title)");

        issueBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        returnBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        viewAllBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        sortByTitleBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        sortByAuthorBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        backBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        linearSearchBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        binarySearchBtn.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0; gbc.gridy = 0; controlPanel.add(issueBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 0; controlPanel.add(returnBtn, gbc);
        gbc.gridx = 2; gbc.gridy = 0; controlPanel.add(sortByTitleBtn, gbc);
        gbc.gridx = 3; gbc.gridy = 0; controlPanel.add(sortByAuthorBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; controlPanel.add(searchField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 1; controlPanel.add(linearSearchBtn, gbc);
        gbc.gridx = 3; gbc.gridy = 1; controlPanel.add(binarySearchBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; controlPanel.add(viewAllBtn, gbc);
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2; controlPanel.add(backBtn, gbc);

        issueBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter title to issue:", "Issue Book", JOptionPane.PLAIN_MESSAGE);
            if (title != null && !title.trim().isEmpty()) {
                Library.issueBook(title.trim());
                updateBookTable(Library.getBooks());
            }
        });

        returnBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter title to return:", "Return Book", JOptionPane.PLAIN_MESSAGE);
            if (title != null && !title.trim().isEmpty()) {
                Library.returnBook(title.trim());
                updateBookTable(Library.getBooks());
            }
        });

        viewAllBtn.addActionListener(e -> updateBookTable(Library.getBooks()));

        sortByTitleBtn.addActionListener(e -> {
            Library.sortByTitle();
            updateBookTable(Library.getBooks());
        });

        sortByAuthorBtn.addActionListener(e -> {
            Library.sortByAuthor();
            updateBookTable(Library.getBooks());
        });

        linearSearchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                String[] options = {"Title", "Author"};
                int choice = JOptionPane.showOptionDialog(this, "Search by:", "Linear Search",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                List<Book> results = new ArrayList<>();
                if (choice == 0) { // Title
                    results = Library.linearSearch(query, "title");
                } else if (choice == 1) { // Author
                    results = Library.linearSearch(query, "author");
                }
                if (!results.isEmpty()) {
                    updateBookTable(results);
                } else {
                    JOptionPane.showMessageDialog(this, "No books found matching your search.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search query.", "Search Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        binarySearchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                Book result = Library.binarySearch(query);
                if (result != null) {
                    List<Book> results = new ArrayList<>();
                    results.add(result);
                    updateBookTable(results);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found using binary search (exact title match required).", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a title for binary search.", "Search Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new RoleSelector();
        });

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
        updateBookTable(Library.getBooks());
    }

    private void updateBookTable(List<Book> booksToDisplay) {
        tableModel.setBookList(booksToDisplay);
    }
}
