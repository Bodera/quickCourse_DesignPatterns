# Builder Introduction

The first design pattern we are going to see is the Builder. Some motivations behinds a builder are:

- Some objects are simple and can be created in a single constructor call.
- Other objects require a lot of cerimony to create. For example when you are constructing a String from a dozen different pieces what you typically want to do is not call a constructor, and not do a concatenation where you have eleven plus signs, instead you make a `StringBuilder` which actually let's you construct an object piece by piece.
- Having an object with too much constructor arguments is not productive. That's simply because people make mistakes when they see too much variety. If you have a constructor with arguments you may miss up one of the arguments by misplacing them and once again code completion model it's great but not that great and may happens that they give you a false sense of security in a way.
- Instead, opt for piecewise construction.
- Builder provides an API for constructing an object step-by-step. When piecewise object construction is complicated, Builder provides an API for doing it succinctly.

## Hands-on

The best way of getting acquainted with the builder design pattern is of course to take a look at one of the builders that are actually part of the JDK. So let's jump into the code editor.

Let's imagine that you're making some sort of application for serving web pages, a fairly common concern that if you look at the number of web services that are out there.

So our goal is to output a simple string, maybe a piece of text inside paragraph tags.

```java
class Demo {
    public static void main(String[] args) {
        //simulation of what we can output in our webserver
        String hello = "hello";
        System.out.println("<p>" + hello + "</p>");
    }
}
```

The output we got is ```<p>hello</p>```.

Fairly simple thing to do, but notice that it will only works if you have a very small example. Until now we don't need to provide any sort of object orientation for whatever is that we're doing.

Now let's imagine that the situation becomes a bit more complicated, and what about if we have a list and we want to put a list of words. The first solution to that seems to be starting cocatenating things, but how exactly can I make this a bit more palatable because is really ugly to write something like:

```java
"<ul>%n" + "<li>" + words[0] + "</li>" + "</ul>";
```

For sure we will need to iterate across the indexes of our array and by that we could bunch them all together using something like ```String.join()``` and to finish we could start using ```stream()``` so that you map each of the elements in the array in a list and everything is moving away from the triviality it seemed at the beginning.

So how we can represents a large string an an ordered list of items? Using a *Builder* for sure. As name suggests is used for building strings out of different components, and the key thing about the builder is the this builder, as like any Builder, is that the construction of an object that it builds happens not in a single call but through several functions, several operations, mading it a piecewise process of construction. Hopefully this will become more clear when we look at the Factory Pattern later.

```java
class Demo 
{
    public static void main(String[] args) 
    {
        //simulation of what we can output in our webserver
        String hello = "hello";
        System.out.println("<p>" + hello + "</p>");

        //avoiding concatenation
        String [] words = {"hello", "world"};
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>\n");
        for (word : words) {
            sb.append(String.format("  <li>%s</li>%n", word));
        }
        sb.append("</ul>");

        System.out.println(sb); //behing the scenes calls .toString()
    }
}
```

Expected output is:
```txt
<p>hello</p>

<ul>
  <li>hello</li>
  <li>world</li>
</ul>
```

The variable ```sb``` is what actually builds the overall object what gives you the final result so to speak. That's the behaviour of real Builders, by calling a particular function which gives you the final build up result unless of course the result is already stored inside the object in its final form which may or may not be the case. I mean if you are modeling in an object oriented like we're going to do in next class then you don't have the final result, and so you have to call a special method in order to actually get the final result.

You must agree that's way more simpler and readable than performing concatenation using the plus operator on Strings. By the way it's in fact more memory efficient but we're going to ignore that for now.

So essentially a `StringBuilder` is built-in Builder specifically for Strings, and in a similar fashion you can have your own builders which instead of building primitives like a String they actually build the kind of domain objects that you are actually using in your application and that's what we're going to look at next.
