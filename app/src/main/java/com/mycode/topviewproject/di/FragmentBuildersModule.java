package com.mycode.topviewproject.di;

import com.mycode.topviewproject.ui.Grid1;
import com.mycode.topviewproject.ui.Grid2;
import com.mycode.topviewproject.ui.Grid3;
import com.mycode.topviewproject.ui.Grid4;
import com.mycode.topviewproject.ui.Grid5;
import com.mycode.topviewproject.ui.Grid6;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract Grid1 contributesGrid1();

    @ContributesAndroidInjector
    abstract Grid2 contributesTab2();

    @ContributesAndroidInjector
    abstract Grid3 contributesTab3();

    @ContributesAndroidInjector
    abstract Grid4 contributesTab4();

    @ContributesAndroidInjector
    abstract Grid5 contributesTab5();

    @ContributesAndroidInjector
    abstract Grid6 contributesTab6();

}