# SearchEngine

This project is a console-based search engine developed in Java with a menu-driven interface. It reads data from a file using the --data argument and loads all records at startup. The application implements an inverted index to enable fast and efficient searching without scanning the full dataset each time. It supports three search strategies: ALL, ANY, and NONE, allowing flexible query handling. All searches are case-insensitive and optimized for performance.

The project is structured into separate components for better readability and maintainability. It is fully covered with JUnit unit tests to ensure correctness of all functionalities. Code quality is enforced using PMD, SpotBugs, and Checkstyle, integrated via Maven. Test coverage is measured with JaCoCo, achieving at least 80% coverage.
