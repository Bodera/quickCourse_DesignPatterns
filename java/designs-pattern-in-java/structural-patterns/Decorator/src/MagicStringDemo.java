import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles.Lookup;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MagicStringDemo
{
    public static void main(String[] args) {
        MagicString s = new MagicString("Hello World");
        System.out.println(s + " has " + s.getNumberOfVowels() + " vowels.");
    }    
}

class MagicString
{
    private String myStr;

    public MagicString(String str)
    {
        myStr = str;
    }

    public long getNumberOfVowels()
    {
        return myStr.chars().filter(c -> "aeiouAEIOU".indexOf(c) != -1).count();
    }

    @Override
    public String toString()
    {
        return myStr;
    }

    //------------ D E L E G A T E D    M E T H O D S ------------//
    public char charAt(int index) {
        return myStr.charAt(index);
    }

    public int codePointAt(int index) {
        return myStr.codePointAt(index);
    }

    public int length() {
        return myStr.length();
    }

    public boolean isEmpty() {
        return myStr.isEmpty();
    }

    public int codePointBefore(int index) {
        return myStr.codePointBefore(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return myStr.codePointCount(beginIndex, endIndex);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return myStr.offsetByCodePoints(index, codePointOffset);
    }

    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        myStr.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public void getBytes(int srcBegin, int srcEnd, byte[] dst, int dstBegin) {
        myStr.getBytes(srcBegin, srcEnd, dst, dstBegin);
    }

    public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
        return myStr.getBytes(charsetName);
    }

    public byte[] getBytes(Charset charset) {
        return myStr.getBytes(charset);
    }

    public byte[] getBytes() {
        return myStr.getBytes();
    }

    public boolean equals(Object anObject) {
        return myStr.equals(anObject);
    }

    public boolean contentEquals(StringBuffer sb) {
        return myStr.contentEquals(sb);
    }

    public boolean contentEquals(CharSequence cs) {
        return myStr.contentEquals(cs);
    }

    public boolean equalsIgnoreCase(String anotherString) {
        return myStr.equalsIgnoreCase(anotherString);
    }

    public int compareTo(String anotherString) {
        return myStr.compareTo(anotherString);
    }

    public int compareToIgnoreCase(String str) {
        return myStr.compareToIgnoreCase(str);
    }

    public boolean regionMatches(int toffset, String other, int ooffset, int len) {
        return myStr.regionMatches(toffset, other, ooffset, len);
    }

    public boolean regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len) {
        return myStr.regionMatches(ignoreCase, toffset, other, ooffset, len);
    }

    public boolean startsWith(String prefix, int toffset) {
        return myStr.startsWith(prefix, toffset);
    }

    public boolean startsWith(String prefix) {
        return myStr.startsWith(prefix);
    }

    public boolean endsWith(String suffix) {
        return myStr.endsWith(suffix);
    }

    public int hashCode() {
        return myStr.hashCode();
    }

    public int indexOf(int ch) {
        return myStr.indexOf(ch);
    }

    public int indexOf(int ch, int fromIndex) {
        return myStr.indexOf(ch, fromIndex);
    }

    public int indexOf(int ch, int beginIndex, int endIndex) {
        return myStr.indexOf(ch, beginIndex, endIndex);
    }

    public int lastIndexOf(int ch) {
        return myStr.lastIndexOf(ch);
    }

    public int lastIndexOf(int ch, int fromIndex) {
        return myStr.lastIndexOf(ch, fromIndex);
    }

    public int indexOf(String str) {
        return myStr.indexOf(str);
    }

    public int indexOf(String str, int fromIndex) {
        return myStr.indexOf(str, fromIndex);
    }

    public int indexOf(String str, int beginIndex, int endIndex) {
        return myStr.indexOf(str, beginIndex, endIndex);
    }

    public int lastIndexOf(String str) {
        return myStr.lastIndexOf(str);
    }

    public int lastIndexOf(String str, int fromIndex) {
        return myStr.lastIndexOf(str, fromIndex);
    }

    public String substring(int beginIndex) {
        return myStr.substring(beginIndex);
    }

    public String substring(int beginIndex, int endIndex) {
        return myStr.substring(beginIndex, endIndex);
    }

    public CharSequence subSequence(int beginIndex, int endIndex) {
        return myStr.subSequence(beginIndex, endIndex);
    }

    public String concat(String str) {
        return myStr.concat(str);
    }

    public String replace(char oldChar, char newChar) {
        return myStr.replace(oldChar, newChar);
    }

    public boolean matches(String regex) {
        return myStr.matches(regex);
    }

    public boolean contains(CharSequence s) {
        return myStr.contains(s);
    }

    public String replaceFirst(String regex, String replacement) {
        return myStr.replaceFirst(regex, replacement);
    }

    public String replaceAll(String regex, String replacement) {
        return myStr.replaceAll(regex, replacement);
    }

    public String replace(CharSequence target, CharSequence replacement) {
        return myStr.replace(target, replacement);
    }

    public String[] split(String regex, int limit) {
        return myStr.split(regex, limit);
    }

    public String[] splitWithDelimiters(String regex, int limit) {
        return myStr.splitWithDelimiters(regex, limit);
    }

    public String[] split(String regex) {
        return myStr.split(regex);
    }

    public String toLowerCase(Locale locale) {
        return myStr.toLowerCase(locale);
    }

    public String toLowerCase() {
        return myStr.toLowerCase();
    }

    public String toUpperCase(Locale locale) {
        return myStr.toUpperCase(locale);
    }

    public String toUpperCase() {
        return myStr.toUpperCase();
    }

    public String trim() {
        return myStr.trim();
    }

    public String strip() {
        return myStr.strip();
    }

    public String stripLeading() {
        return myStr.stripLeading();
    }

    public String stripTrailing() {
        return myStr.stripTrailing();
    }

    public boolean isBlank() {
        return myStr.isBlank();
    }

    public Stream<String> lines() {
        return myStr.lines();
    }

    public String indent(int n) {
        return myStr.indent(n);
    }

    public String stripIndent() {
        return myStr.stripIndent();
    }

    public String translateEscapes() {
        return myStr.translateEscapes();
    }

    public <R> R transform(Function<? super String, ? extends R> f) {
        return myStr.transform(f);
    }

    public IntStream chars() {
        return myStr.chars();
    }

    public IntStream codePoints() {
        return myStr.codePoints();
    }

    public char[] toCharArray() {
        return myStr.toCharArray();
    }

    public String formatted(Object... args) {
        return myStr.formatted(args);
    }

    public String intern() {
        return myStr.intern();
    }

    public String repeat(int count) {
        return myStr.repeat(count);
    }

    public Optional<String> describeConstable() {
        return myStr.describeConstable();
    }

    public String resolveConstantDesc(Lookup lookup) {
        return myStr.resolveConstantDesc(lookup);
    }
}
