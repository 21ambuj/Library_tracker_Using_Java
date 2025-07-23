
# Library Book Tracker

A desktop application for managing library inventory and circulation, built in Java with Swing. It features role-based access (Admin/Student), an immersive fullscreen UI, and core Data Structures & Algorithms for efficient operations.

---

## Table of Contents

* [Overview](#overview)
* [Features](#features)
* [Architecture & Design](#architecture--design)
* [Data Structures & Algorithms](#data-structures--algorithms)
* [UI/UX Highlights](#uiux-highlights)
* [Getting Started](#getting-started)
* [Acknowledgements](#acknowledgements)
* [License](#license)

---

## Overview

This project was developed as a hands‑on learning endeavor, under the guidance of an experienced mentor and with support from advanced AI tools. It delivers a clean, intuitive interface alongside robust back‑end logic to handle book records, searches, sorting, and circulation workflows.

---

## Features

### General

* **Role-Based Dashboards**
  Separate Admin and Student views, each with tailored functionality.
* **Fullscreen Mode**
  Optimizes real estate for an immersive experience.
* **Modern Look & Feel**
  Applies the Nimbus L\&F for a clean, contemporary UI.
* **Intuitive Navigation**
  Seamless switching between roles and core features.

### Admin Dashboard

* **Add & Remove Books**
  Register new books or delete existing entries.
* **Issue & Return**
  Track book circulation status per student.
* **View Inventory**
  Display all books with status indicators.

### Shared: Search & Sort

* **Linear Search**
  Partial matches by title or author (unsorted data).
* **Binary Search**
  Fast lookup by exact title (on sorted list).
* **Sort by Title / Author**
  Alphabetical ordering via `Collections.sort()` and custom `Comparator`.

---

## Architecture & Design

* **Language & Framework**
  Java 8+ • Swing (JFrame, JTable, JOptionPane)
* **Storage**
  `ArrayList<Book>` for dynamic in‑memory book records.
* **Table Model**
  Custom `BookTableModel` extending `AbstractTableModel` for UI binding.
* **Layout**
  `GridBagLayout` ensures responsive control placement across resolutions.

---

## Data Structures & Algorithms

* **Dynamic Storage**
  `ArrayList` allows O(1) amortized adds/removals.
* **Efficient Sorting**
  `Collections.sort(list, comparator)` (Timsort) yields O(n log n) performance.
* **Targeted Searches**

  * *Linear Search*: O(n), supports partial queries.
  * *Binary Search*: O(log n), requires pre-sorted list copy for exact-title lookup.

---

## UI/UX Highlights

* **JFrame-Based Screens**
  Distinct windows for Role Selection, Admin, and Student views.
* **JTable Presentation**
  Structured columns for Title, Author, and Status with live updates.
* **Nimbus Look & Feel**
  Consistent styling across components.
* **Dialog Interactions**
  `JOptionPane` for lightweight input and feedback.

---

## Getting Started

### Prerequisites

* **Java Development Kit** (JDK 8 or higher)
* **Git** (for cloning the repository)

### Installation

```bash
git clone https://github.com/21ambuj/Library_tracker_Using_Java
cd Library_tracker_Using_Java
```

### Compilation & Execution

```bash
# Compile all Java sources
javac *.java

# Launch the application
java LibraryApp
```

The application will launch in fullscreen, beginning with the Role Selection screen.

---

## Acknowledgements

* **Mentor:** For expert guidance and ongoing support.
* **AI Assistance:** For refining code snippets, UI layouts, and algorithmic logic.

---

## License

This project is distributed under the [MIT License](./LICENSE). Feel free to use, modify, and distribute as permitted.
