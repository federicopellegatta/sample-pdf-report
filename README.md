# PDF Report Generator with PDFBox and Thymeleaf

The Spring Boot PDF Report Generator is a sample Java application that leverages the power
of [OpenHTMLtoPDF](https://github.com/danfickle/openhtmltopdf), [PDFBox](https://pdfbox.apache.org/)
and [Thymeleaf](https://www.thymeleaf.org/) to dynamically create PDF reports.
This application is designed to simplify the process of generating professional-quality PDF documents from your data,
making it an ideal solution for businesses, developers, and organizations seeking to automate their report generation
workflows.

This application, specifically designed as a sample, showcases the capabilities of these technologies through a
simulated school register scenario. It demonstrates how these tools can be employed to create student progress
reports, class schedules, and other educational documents.

## Getting Started

### Requirements

To run this application, you will need the following:

1. **Java Development Kit (JDK):** This application is built using Java, so you will need to have the JDK installed on
   your machine. The recommended version is JDK 17 or later.

2. **Maven:** This project uses Maven for dependency management. Make sure you have it installed and properly
   configured.

3. **Docker:** Docker is used to run a PostgreSQL database for this sample application. Make sure you have Docker
   installed and properly configured on your machine. You will need to run `docker-compose up` in the project directory
   to start the
   database.

4. **PDFBox, OpenHTMLtoPDF, and Thymeleaf:** These libraries are used for PDF generation and HTML templating. They are
   included as dependencies in the Maven `pom.xml` file, so they will be automatically downloaded when you build the
   project.

5. **IDE:** An Integrated Development Environment (IDE) that supports Java, such as IntelliJ IDEA, is recommended for
   running and modifying this application.

Please ensure that all these requirements are met before attempting to run or modify this application.

### Running the Application

To start the application, navigate to the project directory and follow these steps:

1. **Start the PostgreSQL database:** Run the command `docker-compose up`. This will start the PostgreSQL database in a
   Docker container.

2. **Build the application:** Download all necessary dependencies and build the application running the
   command `mvn clean install`.

3. **Run the application:** After the build is successful, you can start the application by running the
   command `java -jar target/samplepdfreport-0.0.1-SNAPSHOT.jar`.

4. **Access the application:** Once the application is running, you can access it by opening a web browser and
   navigating to `http://localhost:8081`. You can also change the port number in the `application.yml` file.

5. **Generate a report:** To generate a report, you need to execute a get request to the `/api/v1/pdf-generator`
   endpoint. You can do this by opening a web browser and navigating
   to [http://localhost:8081/api/v1/pdf-generator](http://localhost:8081/api/v1/pdf-generator). This will generate a PDF
   report and download it to your machine.

You can download [`examples/output.pdf`](examples/output.pdf) to preview an example of the generated PDF report 

## Key Features

1. **Spring Boot Framework:** This sample is built on the Spring Boot framework, providing a robust foundation for
   development and integration with other Spring Boot projects.

2. **PDFBox Integration:** PDFBox, a powerful PDF library, is utilized to generate PDF reports. The sample illustrates
   how this integration simplifies the creation and manipulation of PDF documents.

3. **OpenHTMLtoPDF Integration:** OpenHTMLtoPDF is a library to convert HTML into PDF or images. This allows for
   easy and flexible design of report templates using HTML and CSS, which are then seamlessly converted into
   high-quality PDF reports.

4. **Thymeleaf Templating:** Thymeleaf, an HTML templating engine, is used for designing and formatting the PDF reports.
   The sample shows how Thymeleaf's flexibility can be leveraged to customize report templates according to specific
   educational needs.

5. **Dynamic Data Binding:** The sample highlights the ability to populate reports with dynamic data, making it easy to
   generate personalized student records and class-related documents.

6. **Custom Styling:** Users can observe how custom styles, fonts, and layouts can be applied to the sample reports to
   match branding or design preferences.