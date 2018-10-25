# RemaTransactionParser

RemaTransactionParser is an utility for converting the JSON files exported by the GDRP data export tool of Rema 1000 (Norwegian grocery store) to more easily digestible formats - Excel 2003+ (XLSX) or a Database Import Script (SQL).

Once the data has been exported to Excel or a Database, it may be further analyzed to calculate the amount spent per product (or a group of products) in any given period. This may be useful for finding ways of reducing your monthly food expenditures (at least in Rema 1000 stores).

# Getting started
## JSON File
Due to new GDPR regulations, Rema 1000 has added an automated mechanism for retrieving all the transaction data associated with your user in the "Æ" Customer Loyalty App/Program. Depending on how much you do your grocery shopping in Rema 1000, this may be every grocery shopping trip since 2017, broken into invidual transactions and receipt entries.

To request this data, open the "Æ" app, then click the "profile" button to the right in the bottom menu bar. Then choose _"Vilkår og personvern"_, and click the blue button _"Vis mine data"_ in the resulting window. Click _"Gi meg innsyn i mine data"_, and enter an email address where you want the ZIP-file with the exported data to be sent.

Rema 100 will also send a password by SMS to the same phone number during login in the "Æ" app. This password must be used to decompress the ZIP-file, and extract the containing JSON-file.

## Install
Before running RemaTransactionParser, first ensure that you have Java 8 or later installed on your system (check by running _java --version_). Download the latest version of Java either from java.com, adoptopenjdk.net or using the package manager of your operating system (if any).

To install RemaTransactionParser, download the latest release from "Releases", and simply extract (or build - see below) RemaTransactionParser to a folder. Optionally, you may add this folder to your PATH variable. Otherwise, always specify RemaTransactionParser by its full file path, or navigate to the folder (_cd_) in the command line.

## Running
Executing the following command in the command line will convert the JSON-file to an Excel/SQL file. Substitute _source-json_ and _destination-file_ with the corresponding correct file paths:
```bat
RemaTransactionParser source-json destination-file
```
For Unix/Mac, use the included SH script:
```batch
./RemaTransactionParser.sh source-json destination-file
```
The file extension of _destination-file_ specifies the output format - either XLSX or SQL. You may override this behavior by using the -format flag.

## Command Line
The full documentation of the command line arguments:
```
RemaTransactionParser [-f format] [-s] [-h] source destination
 -f format     Specify the output format, either XLSX (Excel 2003) or SQL
                (Database Export script for SQLite). If not specified, the
                output file extension will be used instead.
 -s            Enable stream mode, allowing the program to use standard
                output or standard input instead of the file system. Format
                must be specified if no output file is specified.
 -h            Show this help text.
 source        Path to the JSON-file with the exported Rema 1000 data.
                May be omitted in stream mode.
 destination   Path to the output XLSX- or SQL-file where the conversion
                output will be written. May be omitted in stream mode.
```

# Building
The following dependencies must be installed to build RemaTransactionParser:
  - [Java 8](https://www.oracle.com/technetwork/java/javase/downloads/index.html) or later
  - [Maven](https://maven.apache.org/)

Then run _mvn package_ in the root RemaTransactionParser folder (with pom.xml). The final shaded JAR file can be found in _target/RemaTransactionParser.jar_.

Script files (.bat or .sh) are stored inside _release/_.

License
----
GPLv2
