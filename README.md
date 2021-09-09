# coded-with
Coded-With is a tool recognition program.

In order to analyse a project, the next command should be executed from the terminal:

java -jar coded-with.jar path/to/repository path/to/technologie

INFO:
path/to/repository can be the path to a local file or a GitHub repository.
path/to/technologies can be the path to a local JSON file containing the sought tools or the string "default".

In order to receive the top 10 tools analysed by Coded-With, simply run the command without any arguments.

IMPORTANT:
The connection to MongoDB must be established before running the project.
MongoDB has to be installed on the device and the command mongod must be executed from its bin folder.
