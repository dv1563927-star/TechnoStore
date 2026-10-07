package modelo;

import java.util.concurrent.atomic.AtomicInteger;

public class GeneradorIds {
    private static final AtomicInteger CONTADOR = new AtomicInteger(0);
    
    private GeneradorIds() {
        //evita que se instancie
    }
    
    public static int nuevaId() {
        return CONTADOR.incrementAndGet();
    }
}
