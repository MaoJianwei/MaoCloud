package org.mao.cloud.cli.base;

import org.apache.karaf.shell.console.AbstractAction;

/**
 * Created by mao on 2016/10/16.
 */
public abstract class AbstractShellCommand extends AbstractAction{

    public <T> T getService(Class<T> serviceClass){
        return DefaultServiceDirectory.getService(serviceClass);
    }

    public void print(String format, Object... args){
        System.out.println(String.format(format, args));
    }

    private void error(String format, Object... args){
        System.out.println(String.format(format, args));
    }

    protected abstract void execute();

    @Override
    protected Object doExecute() throws Exception {
        try{
            execute();
        }catch(ServiceNotFoundException e){
            error(e.getMessage());
        }
        return null;
    }
}
