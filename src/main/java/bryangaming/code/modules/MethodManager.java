package bryangaming.code.modules;

import bryangaming.code.Manager;
import bryangaming.code.modules.player.PlayerMessage;

public class MethodManager
{
    private PlayerMessage playerMessage;
    private PowerMethod powerMethod;
    private RankMethod loopMethod;
    private CooldownMethod cooldownMethod;

    private Manager manager;

    public MethodManager(final Manager manager) {
        this.manager = manager;
        setup();
    }

    public void setup(){
        playerMessage = new PlayerMessage(manager);
        loopMethod = new RankMethod(manager);
        powerMethod = new PowerMethod(manager);
        cooldownMethod = new CooldownMethod(manager);
    }

    public RankMethod getLoopMethod() {
        return this.loopMethod;
    }

    public PlayerMessage getSender() {
        return playerMessage;
    }

    public PowerMethod getPowerMethod() {
        return powerMethod;
    }

    public CooldownMethod getCooldownMethod(){
        return cooldownMethod;
    }
}
