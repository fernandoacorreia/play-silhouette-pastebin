package app

import com.google.inject.Guice
import play.api.GlobalSettings
import util.di.BaseModule

/**
 * The global configuration.
 */
object Global extends GlobalSettings {

  /**
   * The Guice injector.
   */
  val Injector = createInjector

  /**
   * Loads the controller classes with the Guice injector. So it's possible to inject dependencies
   * directly into the controller.
   *
   * @param controllerClass The controller class to instantiate.
   * @return The instance of the controller class.
   * @throws Exception if the controller couldn't be instantiated.
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    Injector.getInstance(controllerClass)
  }

  /**
   * Create the injector instance.
   *
   * @return The injector instance.
   */
  private def createInjector = Guice.createInjector(new BaseModule)
}
