package Controller;

import Model.Campeonato;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronizer {
    public ReentrantLock rl;
    public Condition cond;
    public Campeonato camp;

    public ReentrantLock campRL;
    public Condition campCond;
    public Synchronizer(Campeonato camp){
        this.rl = new ReentrantLock();
        this.cond = this.rl.newCondition();
        this.campRL = new ReentrantLock();
        this.campCond = this.campRL.newCondition();
        this.camp = camp;
    }

    public void waitCampeonato(String email) throws InterruptedException {
        this.rl.lock();
        while(camp.isRunning()) {
            System.out.println("Thread waiting "+email);
            this.cond.await();
        }
        System.out.println("Thread resumed "+email);
        this.rl.unlock();
    }

    public void endCampeonato(){
        this.rl.lock();
        this.cond.signalAll();
        this.rl.unlock();
    }

    public void playerJoin(){
        this.campRL.lock();
        this.campCond.signal();
        this.campRL.unlock();
    }
    public void waitFull() throws InterruptedException {
        this.campRL.lock();
        while(!camp.isFull()) {
            System.out.println("Thread waiting "+camp.filled());
            this.campCond.await();
        }
        System.out.println("Thread resumed ");
        this.campRL.unlock();
    }

}
