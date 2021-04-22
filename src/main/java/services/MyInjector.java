package services;

import com.google.inject.AbstractModule;

public class MyInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileService.class).to(FileServiceImpl.class).asEagerSingleton();
        bind(TechnologyService.class).to(TechnologyServiceImpl.class).asEagerSingleton();
    }
}