package services;

import com.google.inject.AbstractModule;
import helpers.CodedWithProgram;
import helpers.MyHelper;
import services.basic.BasicFileService;
import services.basic.BasicFileServiceImpl;
import services.basic.BasicTechnologyServiceImpl;
import services.database.MongoService;
import services.database.MongoServiceImpl;
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
        bind(MongoService.class).to(MongoServiceImpl.class).asEagerSingleton();
        bind(MyHelper.class).to(CodedWithProgram.class).asEagerSingleton();
    }
}