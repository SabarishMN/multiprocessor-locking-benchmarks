# Multiprocessor Locking & Synchronization

## Downloading this template

If you know `git`, you can clone this repository and work on it.

Otherwise, click the `Clone or download` button on the repository page and then click `Download zip` to download a zip file.   

## Building and Running the template

The below instructions should be run from the top-level directory.

### Running Unit Test

Run the following command to run the unit test:

Linux: `./gradlew test`

Windows: `./gradlew.bat test`

### Building the project and producing binaries:

Linux: ` ./gradlew build -x test`

Windows: `./gradlew.bat build -x test`

This will produce the binaries in the build folder.

### Running Benchmark main programs:

__Benchmark__:
`java -cp build/libs/homework4.jar edu.vt.ece.hw4.Benchmark <YOUR_ARGS>`

## Intellij

This gradle project can be imported into Intellj by going to `File -> Open` and choosing this directory. As soon as you open the project, a pop-up dialog may appear in the bottom right of the screen asking to import the project with Gradle. Accept it.

For more instructions, go to https://www.jetbrains.com/help/idea/gradle.html#gradle_import_project_start
