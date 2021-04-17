package haylahay.testdrivendev.ui.main


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import haylahay.testdrivendev.R
import junit.framework.TestCase
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest : TestCase() {

    public override fun setUp() {
        super.setUp()
    }

    public override fun tearDown() {}

    fun test_companies_are_loaded_and_displayed() {

        onView(withId(R.id.companiesRV)).check()

    }
}