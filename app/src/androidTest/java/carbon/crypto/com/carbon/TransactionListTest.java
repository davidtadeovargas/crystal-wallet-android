package carbon.crypto.com.carbon;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import carbon.crypto.com.carbon.Assertions.RecyclerViewItemsCountAssertion;
import cy.agorise.crystalwallet.IntroActivity;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.dao.CrystalDatabase;
import cy.agorise.crystalwallet.models.CryptoCoinTransaction;
import cy.agorise.crystalwallet.randomdatagenerators.RandomTransactionsGenerator;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by Henry Varona on 19/9/2017.
 */
@RunWith(AndroidJUnit4.class)
public class TransactionListTest {
    CrystalDatabase db;

    List<CryptoCoinTransaction> transactions;
    int numberOfTransactions = 100;

    @Before
    public void addingTransactions(){
        db = CrystalDatabase.getAppDatabase(InstrumentationRegistry.getTargetContext());
        transactions = RandomTransactionsGenerator.generateTransactions(numberOfTransactions,1262304001,1496275201,1,999999999);

        for(int i=0;i<transactions.size();i++) {
            db.transactionDao().insertTransaction(transactions.get(i));
        }
    }

    @Rule
    public ActivityTestRule<IntroActivity> activityTestRule = new ActivityTestRule<IntroActivity>(IntroActivity.class);

    @Test
    public void numberOfTransactionsInList(){
        onView(withId(R.id.transactionListView)).check(new RecyclerViewItemsCountAssertion(numberOfTransactions));
    }

    @After
    public void deleteTransactions(){
        this.db.transactionDao().deleteAllTransactions();
    }
}
