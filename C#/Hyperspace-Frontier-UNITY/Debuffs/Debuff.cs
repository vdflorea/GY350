using UnityEngine;

/**
 * Base class (parent) for debuffs
 */
public abstract class Debuff : MonoBehaviour
{
    protected abstract void ApplyDebuff();
    protected abstract void UpdateUI();

    private void OnTriggerEnter(Collider other)
    {
        if (!other.CompareTag("Spaceship")) return;
        
        ApplyDebuff();
        UpdateUI();
        SoundManager.instance.PlayDebuffSound();
        Destroy(gameObject);
    }
}
