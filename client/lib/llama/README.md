# Prebuilt Llama interface

This directory contains some pre-compiled jars for llamacpp-java. You can
set Maven to use one of these using `mvn install:install-file`. For example,
to install the NVidia Windows Llama jar, you would run:

```
mvn install:install-file -Dfile='lib/llama/llama-win-nv-2.2.1.jar' -DgroupId='de.kherud' -DartifactId=llama -Dversion='2.2.1' -Dpackaging=jar
```
