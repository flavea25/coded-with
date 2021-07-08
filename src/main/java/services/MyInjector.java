package services;

import com.google.inject.AbstractModule;
import services.basic.BasicFileService;
import services.basic.BasicFileServiceImpl;
import services.basic.BasicTechnologyServiceImpl;
import services.repository.GithubFileServiceImpl;
import services.repository.RepositoryFileService;

public class MyInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(BasicFileService.class).to(BasicFileServiceImpl.class).asEagerSingleton();
        bind(RepositoryFileService.class).to(GithubFileServiceImpl.class).asEagerSingleton();
        bind(TechnologyService.class).to(BasicTechnologyServiceImpl.class).asEagerSingleton(); //TODO change to GithubTechnologyServiceImpl
    }
}