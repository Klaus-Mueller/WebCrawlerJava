# Web Crawler

## Overview
The Web Crawler is a Java program designed to fetch web pages from a specified URL and extract useful information, such as links and keywords, from those pages. It operates by traversing the web pages in a systematic manner, following links, and analyzing content to gather data.

## Features
- Fetch web pages from a given URL
- Extract links from the fetched pages
- Search for specific keywords within the page content
- Multithreaded crawling for improved performance
- Concurrent processing of multiple URLs

## Installation
1. Clone the repository to your local machine.
2. Ensure you have Java installed on your system.
3. Ensure the BASE_URL env is set to the base URL.
4. Build the project.

## Usage
1. Open http://localhost:4567/
2. Use the Web Crawler UI Page to add processes.
![image](https://github.com/Klaus-Mueller/WebCrawlerJava/assets/6924934/8060e860-6e9f-476d-8fc2-6e5de83c7a6b)


## Documentation

### `WebCrawler`
- **Overview**: The `WebCrawler` class is the core component of the web crawling functionality. It is responsible for fetching web pages, extracting links and keywords, and coordinating the crawling process.
- **Functionality**:
  - Fetch web pages from a specified URL.
  - Extract links from the fetched pages using regular expressions.
  - Search for specific keywords within the page content.
  - Multithreaded crawling for improved performance.
  - Integration with the `WebCrawlerThreadManager` for concurrent processing of multiple URLs.
- **Usage**:
  - Used to initialize crawling tasks and execute the crawling process.
  - Provides methods for processing URLs, extracting links, and checking URL validity.

### `WebCrawlerThreadManager`
- **Overview**: The `WebCrawlerThreadManager` class manages the multithreaded execution of crawling tasks. It controls the thread pool and coordinates the distribution of tasks among available threads.
- **Functionality**:
  - Creates and manages a fixed-size thread pool for executing crawling tasks.
  - Distributes tasks among available threads in the thread pool.
  - Monitors the status of tasks and handles task completion.
- **Usage**:
  - Used by the `WebCrawler` class to execute crawling tasks concurrently.
  - Controls the execution flow and thread management during the crawling process.

### `SearchTask`
- **Overview**: The `SearchTask` class represents a single crawling task. It encapsulates information about the task, such as the keyword to search for and the results obtained.
- **Functionality**:
  - Stores information about the crawling task, including the keyword to search for and the status of the task.
  - Tracks the results obtained during the crawling process.
- **Usage**:
  - Created and managed by the `WebCrawler` class to represent individual crawling tasks.
  - Used to track the progress and results of each crawling task.

### `SearchTaskManager`
- **Overview**: The `SearchTaskManager` class manages the creation and execution of crawling tasks. It provides a high-level interface for starting and monitoring crawling tasks.
- **Functionality**:
  - Creates new crawling tasks with specified keywords.
  - Manages the execution of crawling tasks using the `WebCrawler` and `WebCrawlerThreadManager` classes.
  - Tracks the status and progress of crawling tasks.
- **Usage**:
  - Used by the application to initiate and manage crawling tasks.
  - Provides methods for starting new tasks, monitoring task status, and retrieving task results.

These are the main classes responsible for implementing the web crawling functionality in this application. They work together to fetch web pages, extract keywords, and manage the crawling process efficiently.
