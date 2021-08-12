package services;

import com.google.inject.AbstractModule;
import services.basic.BasicFileService;
import services.basic.BasicFileServiceImpl;
import services.basic.BasicTechnologyServiceImpl;
import services.repository.GithubFileServiceImpl;
import services.repository.GithubTechnologyServiceImpl;
import services.repository.RepositoryFileService;
import services.repository.RepositoryTechnologyService;

public class MyInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(BasicFileService.class).to(BasicFileServiceImpl.class).asEagerSingleton();
        bind(RepositoryFileService.class).to(GithubFileServiceImpl.class).asEagerSingleton();
        bind(RepositoryTechnologyService.class).to(GithubTechnologyServiceImpl.class).asEagerSingleton();
        bind(TechnologyService.class).to(BasicTechnologyServiceImpl.class).asEagerSingleton();
    }
}