package net.classfileparser;

import net.classfileparser.buffer.BigEndianByteArrayReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClassParserApp {


    class Foo {
        private int k;
        private double s;
        private static final float k2 = 1;

        Foo(String s) {
        }

        Foo() {
        }

        synchronized void foo() {
        }

    }


    public static void main(String args[]) throws URISyntaxException, IOException {
        URL url = ClassParserApp.class.getClassLoader().getResource("net/classfileparser/ClassParserApp$Foo.class");
        byte[] content = Files.readAllBytes(Paths.get(url.toURI()));
        ClassStructure clazz = new ClassStructure(new BigEndianByteArrayReader(content));
        clazz.parse();
        clazz.dump();
    }
}
