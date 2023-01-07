package Controller;

import Model.Campeonato;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronizer {
    public ReentrantLock rl;
    public Condition cond;
    public Campeonato camp;

    public Synchronizer(Campeonato camp){
        this.rl = new ReentrantLock();
        this.cond = this.rl.newCondition();
        this.camp = camp;
    }

    public void waitCampeonato() throws InterruptedException {
        this.rl.lock();
        while(camp.isRunning()) {
            this.cond.await();
        }
        this.rl.unlock();
    }

    public void endCampeonato(){
        this.rl.lock();
        this.cond.signalAll();
        this.rl.unlock();
    }

}
