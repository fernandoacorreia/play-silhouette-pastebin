package app

import com.google.inject.Guice
import play.api.GlobalSettings
import com.mohiva.play.silhouette.custom.BaseModule

/**
 * The global configuration.
 */
object Global extends GlobalSettings {

  /**
   * The Guice dependencies injector.
   */
  val injector = Guice.createInjector(new BaseModule)

  /**
   * Loads the controller classes with the Guice injector. So it's possible to inject dependencies
   * directly into the controller.
   *
   * @param controllerClass The controller class to instantiate.
   * @return The instance of the controller class.
   * @throws Exception if the controller couldn't be instantiated.
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A =
    injector.getInstance(controllerClass)

}
