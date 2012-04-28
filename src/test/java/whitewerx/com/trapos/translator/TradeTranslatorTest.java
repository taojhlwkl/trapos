package whitewerx.com.trapos.translator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import whitewerx.com.trapos.model.Amount;
import whitewerx.com.trapos.model.Currency;
import whitewerx.com.trapos.model.CurrencyPair;
import whitewerx.com.trapos.model.Rate;
import whitewerx.com.trapos.model.Trade;
import whitewerx.com.trapos.model.TradeType;
import whitewerx.com.trapos.translators.TradeTranslator;
import whitewerx.com.trapos.translators.TranslateException;

@RunWith(JMock.class)
public class TradeTranslatorTest {
    
    Mockery context = new Mockery();
    
    private static final CurrencyPair EURUSD = new CurrencyPair("EUR", "USD");

    /**
     * Given T|B|5.1t|EURUSD|1.3124
     * 
     * The translated trade should be: Buy 5.1 thousand EUR at @ 1.3124 EURUSD. 
     */
    @Test
    public void translatesATradeMessage() throws Exception {
        String delimitedTrade = "T|B|5.1t|R|EURUSD|1.3124";
        
        Amount fivePointOneThousand = new Amount(5.1 * 1000, new Currency("EUR"));
        Rate atEURUSDRate = new Rate(1.3124, EURUSD);
        final Trade expected = new Trade(TradeType.BUY, fivePointOneThousand, atEURUSDRate);
        
        TradeTranslator t = new TradeTranslator();
        Trade trade = t.translate(delimitedTrade);
        assertThat(trade, equalTo(expected));
    }

    @Test
    public void translatesATradeMessageWithNoMultiplier() throws Exception {
        String delimitedTrade = "T|S|5.1|R|EURUSD|1.3124";
        
        Amount fivePointOne = new Amount(5.1, new Currency("EUR"));
        Rate atEURUSDRate = new Rate(1.3124, EURUSD);
        final Trade expected = new Trade(TradeType.SELL, fivePointOne, atEURUSDRate);
        
        TradeTranslator t = new TradeTranslator();
        Trade trade = t.translate(delimitedTrade);
        assertThat(trade, equalTo(expected));
    }

    
    @Test(expected=TranslateException.class)
    public void translateInvalidTradeMessage() throws Exception {
        String invalidTrade = "T||5.1|R|EURUSD|1.3124";

        TradeTranslator t = new TradeTranslator();
        t.translate(invalidTrade);
    }
}
