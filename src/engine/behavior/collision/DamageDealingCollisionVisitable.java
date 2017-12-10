package engine.behavior.collision;

import engine.behavior.ElementProperty;

/**
 * Deals damage when collided with
 * Could be used for mines, projectiles, etc.
 *
 * @author radithya
 */
public class DamageDealingCollisionVisitable implements CollisionVisitable {

    private double damageToDeal;
    private String audioUrl;

    public DamageDealingCollisionVisitable(
            @ElementProperty(value = "damageToDeal", isTemplateProperty = true) double damageToDeal,
            @ElementProperty(value = "audioUrl", isTemplateProperty = true) String audioUrl) {
        this.damageToDeal = damageToDeal;
        this.audioUrl = audioUrl;
    }

    @Override
    public void accept(CollisionVisitor v) {
        v.visit(this);
    }

    double getDamageToDeal() {
        return damageToDeal;
    }

	@Override
	public String getAudioUrl() {
		return audioUrl;
		
	}

}
