/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import mygame.superPower.*;

/**
 *
 * @author ZePedro
 */
public class Player {

    private Spatial[] playerGeo;
    private RigidBodyControl[] playerControl;
    private float mass = 2.0f;
    private Box playerBox;
    private String playerName;
    private Node playerNode;
    private boolean alive = true;
    private Geometry ball;
    private Keys keys;
    private SuperPower sp;
    private float sizeX = 1;
    private float sizeY = 1;
    private float sizeZ = 1;
    private boolean silenced = false;
    private int swapped = 1;
    private boolean immune = false;
    private float velocity = 0.05f;
    private int damage = 1;
    private int model = 0;

    private Material ballMaterial;
    private boolean needChange =false;

    public Player(String name, Vector3f initialPosition, ESuperPower sp, AssetManager assetManager, double rot) {

        playerGeo = new Spatial[5];
        playerControl = new RigidBodyControl[5];

        for (int i = 0; i < 5; i++) {
            playerGeo[i] = assetManager.loadModel("m" + (i + 1) + " abrams.j3o");
            playerGeo[i].setName(name);
            playerGeo[i].setLocalTranslation(initialPosition);
            playerGeo[i].rotate(0.0f, (float)rot, 0.0f);
            playerGeo[i].scale(1.5f);
            //playerGeo.setMaterial(playerMaterial);
            playerControl[i] = new RigidBodyControl(mass);
            playerGeo[i].addControl(playerControl[i]);
            playerControl[i].setKinematic(true);
        }


        playerName = name;
        playerNode = new Node(name);
        switch (sp) {
            case Informatic:
                this.sp = new InformaticSuperPower();

                break;
            case Civil:
                this.sp = new CivilSuperPower();
                break;
            case Chemistry:
                this.sp = new ChemistrySuperPower();
                break;
            case Eletro:
                this.sp = new EletroSuperPower();
                break;
            case Bio:
                this.sp = new BioSuperPower();
                break;
            case Mechanics:
                this.sp = new MechanicsSuperPower();
                break;
            case Metal:
                this.sp = new MetalSuperPower();
                break;
            case None:
            default:
                System.out.println("OOPS");


        }
        this.sp.setType(sp);


        ballMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        ballMaterial.setBoolean("m_UseMaterialColors", true);
        ballMaterial.setColor("m_Ambient", ColorRGBA.Orange);
        ballMaterial.setColor("m_Diffuse", ColorRGBA.Orange);
        ballMaterial.setColor("m_Specular", ColorRGBA.White);
        ballMaterial.setFloat("m_Shininess", 1.0f);
        ballMaterial.setTexture("DiffuseMap", assetManager.loadTexture("cannon.jpg"));
        ballMaterial.setTexture("NormalMap", assetManager.loadTexture("cannon.jpg"));

        playerNode.attachChild(playerGeo[model]);
      
    }

    /**
     * @return the playerGeo
     */
    public Spatial getPlayerGeo() {
        return playerGeo[model];
    }

    /**
     * @return the playerControl
     */
    public RigidBodyControl getPlayerControl() {
        return playerControl[model];
    }

    /**
     * @return the mass
     */
    public float getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     * @return the playerBox
     */
    public Box getPlayerBox() {
        return playerBox;
    }

    /**
     * @param playerBox the playerBox to set
     */
    public void setPlayerBox(Box playerBox) {
        this.playerBox = playerBox;
    }

    public Vector3f getLocalTranslation() {
        return playerGeo[model].getLocalTranslation();
    }

    public void setLocalTranslation(Vector3f translation) {
        playerGeo[model].setLocalTranslation(translation);
    }

    /**
     * @return the playerName
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param playerName the playerName to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * @return the playerNode
     */
    public Node getPlayerNode() {
        return playerNode;
    }

    /**
     * @param playerNode the playerNode to set
     */
    public void setPlayerNode(Node playerNode) {
        this.playerNode = playerNode;
    }

    /**
     * @return the alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * @param alive the alive to set
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * @return the ball
     */
    public Geometry getBall() {
        return ball;
    }

    /**
     * @param ball the ball to set
     */
    public void setBall(Geometry ball) {
        this.ball = ball;
    }

    public int getLeftKey() {
        return keys.getLeftKey();
    }

    public int getRightKey() {
        return keys.getRightKey();
    }

    public int getFireKey() {
        return keys.getFireKey();
    }

    public int getSuperPowerKey() {
        return keys.getSuperPowerKey();
    }

    public void useSuperPower(int pnum) {
        boolean hasMana = sp.getManaCost()<= mygame.Main.manas[pnum].getCurrentMana();
        
        if (!isSilenced() && !isSuperPowerInUse()  && hasMana) {
            this.sp.setInUse(true);
            this.sp.usePower(pnum);
            mygame.Main.manas[pnum].loseMana(sp.getManaCost());
            new CancelSuperPower(sp, sp.getDuration(), pnum).start();
        }

    }

    /**
     * @param keys the keys to set
     */
    public void setKeys(Keys keys) {
        this.keys = keys;
    }

    public void swapKeys() {
        setSwapped(getSwapped() * -1);

    }

    /**
     * @return the swapped
     */
    public int getSwapped() {
        return swapped;
    }

    /**
     * @param swapped the swapped to set
     */
    public void setSwapped(int swapped) {
        this.swapped = swapped;
    }

    public void setImmune(boolean immune) {
        this.immune = immune;
    }

    public boolean isSuperPowerInUse() {
        return this.sp.isInUse();
    }

    /**
     * @return the sizeX
     */
    public float getSizeX() {
        return sizeX;
    }

    /**
     * @return the sizeY
     */
    public float getSizeY() {
        return sizeY;
    }

    /**
     * @return the sizeZ
     */
    public float getSizeZ() {
        return sizeZ;
    }

    

    /**
     * @return the immune
     */
    public boolean isImmune() {
        return immune;
    }

    /**
     * @return the velocity
     */
    public float getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getMoveSpeed() {
        return getVelocity() * getSwapped();
    }

    /**
     * @return the silenced
     */
    public boolean isSilenced() {
        return silenced;
    }

    /**
     * @param silenced the silenced to set
     */
    public void setSilenced(boolean silenced) {
        this.silenced = silenced;
    }

    /**
     * @return the damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @param damage the damage to set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * @return the ballMaterial
     */
    public Material getBallMaterial() {
        return ballMaterial;
    }

    /**
     * @param ballMaterial the ballMaterial to set
     */
    public void setBallMaterial(Material ballMaterial) {
        this.ballMaterial = ballMaterial;
    }

    public SuperPower getSuperPower() {
        return sp;

    }

    public void changeModel(int howMuch,int direction) {
        
       
        model += howMuch*direction;
        if (model <0){
            model = 0;
            return;
        }
        
        Vector3f location = playerGeo[model - howMuch * direction].getLocalTranslation();
        playerNode.detachAllChildren();
        playerNode.attachChild(playerGeo[model]);
        playerGeo[model].setLocalTranslation(location);
    }

    /**
     * @return the needChange
     */
    public boolean isNeedChange() {
        return needChange;
    }

    /**
     * @param needChange the needChange to set
     */
    public void setNeedChange(boolean needChange) {
        this.needChange = needChange;
    }
}
