package technologies;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuildTools {
    MAVEN("pom.xml"),
    GRADLE("build.gradle"),
    ANT("build.xml"),
    GANT("build.gant"),
    GULP1("gulpfile.js"),
    GULP2("gulpfile.ts"),
    GULP3("gulpfile.babel.js"),
    GULP4("Gulpfile.js"),
    SBT("build.sbt"),
    PLEASE1(".plzconfig"),
    PLEASE2("BUILD.plz"),
    PANTS("BUILD"),
    AWS("buildspec.yml"),
    RAKE("Rakefile"),
    ROLLUP("rollup.config.js"),
    CMAKE("CMakeLists.txt"),
    NINJA("build.ninja"),
    GRUNT("Gruntfile.js"),
    PYBUILDER("build.py");

    private String fileName;
}
