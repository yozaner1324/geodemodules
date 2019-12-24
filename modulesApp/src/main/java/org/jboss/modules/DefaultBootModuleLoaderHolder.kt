package org.jboss.modules

import java.lang.reflect.InvocationTargetException
import java.security.AccessController
import java.security.PrivilegedAction

internal object DefaultBootModuleLoaderHolder {
    val INSTANCE: ModuleLoader

    init {
        INSTANCE = AccessController.doPrivileged(PrivilegedAction {
            val loaderClass = System.getProperty("boot.module.loader", LocalModuleLoader::class.java.name)
            try {
                return@PrivilegedAction Class.forName(loaderClass, true, DefaultBootModuleLoaderHolder::class.java.classLoader).asSubclass(ModuleLoader::class.java).getConstructor().newInstance()
            } catch (e: InstantiationException) {
                throw InstantiationError(e.message)
            } catch (e: IllegalAccessException) {
                throw IllegalAccessError(e.message)
            } catch (e: InvocationTargetException) {
                try {
                    throw e.cause!!
                } catch (cause: RuntimeException) {
                    throw cause
                } catch (cause: Error) {
                    throw cause
                } catch (t: Throwable) {
                    throw Error(t)
                }
            } catch (e: NoSuchMethodException) {
                throw NoSuchMethodError(e.message)
            } catch (e: ClassNotFoundException) {
                throw NoClassDefFoundError(e.message)
            }
        })
    }
}