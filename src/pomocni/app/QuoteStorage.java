package pomocni.app;

import glavni.app.Quote;

import java.util.ArrayList;

public class QuoteStorage {

    private ArrayList<Quote> quoteList;
    private static QuoteStorage instance = null;

    public static QuoteStorage getInstance() {
        if(instance == null){
            instance = new QuoteStorage();
        }
        return instance;
    }

    private QuoteStorage(){
        quoteList = new ArrayList<>();
        initQuotes();
    }

    private void initQuotes() {
        quoteList.add(new Quote("Frank Zappa", "So many books, so little time."));
        quoteList.add(new Quote("Marcus Tullius Cicero", "A room without books is like a body without a soul."));
        quoteList.add(new Quote("Bernard M. Baruch", "Be who you are and say what you feel, because those who mind don\'t matter, and those who matter don\'t mind."));
        quoteList.add(new Quote("Dr. Seuss", "You know you\'re in love when you can\'t fall asleep because reality is finally better than your dreams."));
        quoteList.add(new Quote("Mae West", "You only live once, but if you do it right, once is enough."));
        quoteList.add(new Quote("Mahatma Gandhi", "Be the change that you wish to see in the world."));
        quoteList.add(new Quote("Robert Frost", "In three words I can sum up everything I\'ve learned about life: it goes on."));
        quoteList.add(new Quote("Mark Twain", "If you tell the truth, you don\'t have to remember anything."));
        quoteList.add(new Quote("Maya Angelou", "I\'ve learned that people will forget what you said, people will forget what you did, but people will never forget how you made them feel."));
        quoteList.add(new Quote("Elbert Hubbard", "A friend is someone who knows all about you and still loves you."));
    }

    public ArrayList<Quote> getQuoteList() {
        return quoteList;
    }
}
