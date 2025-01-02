using UnityEngine;

/**
 * Base class (parent) for power-ups
 */
public abstract class PowerUp : MonoBehaviour
{
    protected abstract void ApplyPowerUp();
    protected abstract void UpdateUI();

    private void OnTriggerEnter(Collider other)
    {
        if (!other.CompareTag("Spaceship")) return;
        
        ApplyPowerUp();
        UpdateUI();
        SoundManager.instance.PlayPowerUpSound();
        Destroy(gameObject);
    }
}
