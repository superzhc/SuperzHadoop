package net.acesinc.data.json.generator.types;

import com.github.javafaker.Book;

/**
 * @author superz
 * @create 2021/10/21 15:45
 */
public class BookType extends TypeHandler {
    public static final String TYPE_NAME = "book";
    public static final String TYPE_DISPLAY_NAME = "Book";

    private Book book;
    private String type = "";

    public BookType() {
        super();
        book = getFaker().book();
    }

    @Override
    public void setLaunchArguments(String[] launchArguments) {
        super.setLaunchArguments(launchArguments);
        if (launchArguments.length == 1) {
            type = stripQuotes(launchArguments[0]);
        }else{
            type="";
        }
    }

    @Override
    public Object getNextRandomValue() {
        String str = null;
        switch (type) {
            case "author":
                str = book.author();
                break;
            case "publisher":
                str = book.publisher();
                break;
            case "genre":
                str = book.genre();
                break;
            case "title":
            default:
                str = book.title();
        }
        return str;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }
}
