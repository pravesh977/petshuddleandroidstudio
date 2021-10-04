package android.portfolio.petshuddle.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.portfolio.petshuddle.Adapter.ViewPagerAdapter;
import android.portfolio.petshuddle.Fragment.EventFragment;
import android.portfolio.petshuddle.Fragment.MyPetsFragment;
import android.portfolio.petshuddle.Fragment.SearchFragment;
import android.portfolio.petshuddle.Fragment.UserProfileFragment;
import android.portfolio.petshuddle.R;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainTabbedActivity extends AppCompatActivity {

    private ViewPager2 mainViewPager;
    private TabLayout mainTabLayout;
    private ArrayList<String> tabTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        mainViewPager = findViewById(R.id.mainViewPager);
        mainTabLayout = findViewById(R.id.mainTabLayout);

        tabTitles = new ArrayList<>();
        tabTitles.add("My Pets");
        tabTitles.add("Search");
        tabTitles.add("Events");
        tabTitles.add("Profile");

        setViewPagerAdapter();

        new TabLayoutMediator(mainTabLayout, mainViewPager, (tab, position) -> tab.setText(tabTitles.get(position))).attach();

    }

    public void setViewPagerAdapter() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        ArrayList<Fragment> fragmentsList = new ArrayList<>();
        fragmentsList.add(new MyPetsFragment());
        fragmentsList.add(new SearchFragment());
        fragmentsList.add(new EventFragment());
        fragmentsList.add(new UserProfileFragment());
        viewPagerAdapter.setFragments(fragmentsList);
        mainViewPager.setAdapter(viewPagerAdapter);
    }
}