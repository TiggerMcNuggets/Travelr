/**
  * Frontend build commands.
  * Change these if you are using some other package manager. i.e: Yarn
  */
object FrontendCommands {
  val dependencyInstall: String = "npm install"
  val test: String = "npm run test:unit"
  val serve: String = "npm run serve" // NOT USED CURRENTLY
  val build: String = "npm run build"
}