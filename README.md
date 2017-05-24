# Solr Recursive Poster
Expansion of the default post utility for [Solr 1.4](https://wiki.apache.org/solr/Solr1.4) which allows recursive 
posting through file system 

### Usage
You can either build from source or use the jar in bin/jar.

To use the Solr Recursive Poster, run the jar on the command line with the file(s) to 
post as the first argument. To post recursively through a file tree, add the start point
of the file walk as the first argument

```
> jar post.jar /usr/dir
```

### Help
For more help on the command line, run:
```
> jar post.jar -help
```

### Ignored Directories and File Types
If you want to exclude particular file types or whole directories from being 
posted, add them to the post.properties file before compiling. 

At some point I will be adding support for excluding file types and properties 
on the command line or through an external properties file. At this point the only support
is for the properties file in the jar.