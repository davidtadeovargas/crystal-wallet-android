package carbon.crypto.com.carbon;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.Calendar;
import java.util.Date;

import carbon.crypto.com.carbon.Assertions.RecyclerViewItemsCountAssertion;
import cy.agorise.crystalwallet.IntroActivity;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Henry Varona on 19/9/2017.
 */
@RunWith(AndroidJUnit4.class)
public class TransactionListTest {
    CrystalDatabase db;

    @Before
    public void addingTransactions(){
        Calendar cal = Calendar.getInstance();

        this.db = CrystalDatabase.getAppDatabase(InstrumentationRegistry.getTargetContext());
        cal.set(2017,01,01,01,01,01);
        CryptoCoinTransaction newTransaction = new CryptoCoinTransaction();
        newTransaction.setAmount(1);
        newTransaction.setFrom("friend1");
        newTransaction.setTo("me1");
        newTransaction.setDate(cal.getTime());
        db.transactionDao().insertTransaction(newTransaction);

        cal.set(2017,02,02,02,02,02);
        newTransaction = new CryptoCoinTransaction();
        newTransaction.setAmount(2);
        newTransaction.setFrom("friend2");
        newTransaction.setTo("me2");
        newTransaction.setDate(cal.getTime());
        db.transactionDao().insertTransaction(newTransaction);

        cal.set(2017,03,03,03,03,03);
        newTransaction = new CryptoCoinTransaction();
        newTransaction.setAmount(3);
        newTransaction.setFrom("friend3");
        newTransaction.setTo("me3");
        newTransaction.setDate(cal.getTime());
        db.transactionDao().insertTransaction(newTransaction);
    }

    @Rule
    public ActivityTestRule<IntroActivity> activityTestRule = new ActivityTestRule<IntroActivity>(IntroActivity.class);

    @Test
    public void numberOfTransactionsInList(){
        onView(withId(R.id.transactionListView)).check(new RecyclerViewItemsCountAssertion(3));
    }

    @After
    public void deleteTransactions(){
        this.db.transactionDao().deleteAllTransactions();
    }
}
