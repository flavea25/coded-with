package services.repository;

import com.google.inject.Inject;
import services.TechnologyService;
import technologies.Technology;

import java.util.List;

public class GithubTechnologyService extends TechnologyService {

    @Inject
    RepositoryFileService fileService;

    @Override
    public List<Technology> getUsedTechnologies(String root) { //TODO
        return null;
    }
}
