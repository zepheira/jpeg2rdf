

                        +-----------------------------+
                        |         JPEG RDFizer        |
                        +-----------------------------+



  What is this?
  ------------

This is an utility tool to convert the metadata contained in one or more
JPEG image files to RDF. 



  How do I use it?
  ----------------

You launch the RDfizer from the command line by giving it a folder. It will
look for all images that are JPEGs, extract the metadata and dump the RDF
at STDOUT. 

But before you run it, you have to build it.




  How do I build it?
  ------------------
  
This RDFizer requires three things:

 1) a Java Virtual Machine installed on your machine (version 1.4 or greater).
    [type 'java -version' at your shell prompt to know what version you have]
    If you don't have it, go to http://www.java.com and download it.
    
 2) Apache Maven installed (version 2.0 or greater)
    [type 'mvn -version' at your shell prompt to know what version you have]
    If maven is not installed, go to http://maven.apache.org/ and download it.
    Don't panic, the installation is really fast and simple.
    
 3) a network connection (this is because Maven will download the required
    libraries when you build the software)
    
Once you're set (and you have the maven command 'mvn' in your path), 
go to your command shell and type:

  mvn package

this will download the required libraries, compile, package and prepare the
copy the required dependencies in the ./target directory. 
That wasn't that painful, wasn't it?

Now you are ready to launch it, and you can do it by typing

  (unix)  ./jpeg2rdf.sh [folder]
  (win32) .\jpeg2rdf.bat [folder]
  
at the command line.

  
                                  - o -

                                                  Stefano Mazzocchi
                                                <stefanom at mit.edu>

