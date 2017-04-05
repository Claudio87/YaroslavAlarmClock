public class Singleton{
     
    private Singleton(){}
    
    private static class SingletonHolder{
        private final static Singleton instance = new Singletone();
    }
    
    public static Singleton getInstance(){
        return SingletonHolder.instance;
    }

}
