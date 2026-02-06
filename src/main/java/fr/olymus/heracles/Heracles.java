package fr.olymus.heracles;

import fr.olymus.heracles.register.AutoRegistrar;
import fr.olymus.heracles.register.RegisterType;
import fr.olymus.heracles.resources.HeraclesData;

import java.util.concurrent.atomic.AtomicReference;

public class Heracles {

    /**
     * Singleton instance of Heracles
     */
    private static final AtomicReference<Heracles> INSTANCE = new AtomicReference<>();

    private final HeraclesData data;

    /**
     * Private constructor to prevent instantiation
     */
    private Heracles() {
        this.data = new HeraclesData();
    }

    /**
     * Get the singleton instance of Heracles
     * @return Heracles instance
     * @throws IllegalStateException if Heracles is already initialized
     */
    public static Heracles init(){
        Heracles create = new Heracles();
        if(!INSTANCE.compareAndSet(null, create)){
            throw new IllegalStateException("Heracles is already initialized");
        }
        return create;
    }

    /**
     * Automatically register components by scanning the specified base packages for annotated classes.
     * @param type The type of components to register. see {@link RegisterType}.
     * @param basePackages The base packages to scan for components.
     */
    public static void autoRegister(RegisterType type, String... basePackages){
        AutoRegistrar.register(type, basePackages);
    }

    /**
     * Get the singleton instance of Heracles
     * @return Heracles instance
     * @throws IllegalStateException if Heracles is not initialized
     */
    private static Heracles getInstance(){
        Heracles instance = INSTANCE.get();
        if(instance == null){
            throw new IllegalStateException("Heracles is not initialized");
        }
        return instance;
    }

    /**
     * Get the HeraclesData instance
     * @return HeraclesData instance
     */
    public static HeraclesData getData(){
        return getInstance().data;
    }


}