using UnityEngine;

public class Speed_Debuff : Debuff
{
    protected override void ApplyDebuff()
    {
        // Multiply speed multiplier by 2 (double speed)
        // -> NOTE: Only up to 2x speed
        if (GameManager.instance.speedMultiplier < 2)
        {
            GameManager.instance.speedMultiplier *= 2;
        }
    }

    protected override void UpdateUI()
    {
        // Speed is in a debuffed state
        if (GameManager.instance.speedMultiplier > 1)
        {
            GameManager.instance.speedText.color = Color.red;
            GameManager.instance.speedTimerIsRunning = true;
            GameManager.instance.speedMultiplierCountdown = 5f;
        }
        // Speed multiplier in powered-up state initially; Now back to normal after collecting debuff
        else if (GameManager.instance.speedMultiplier == 1)
        {
            GameManager.instance.speedText.color = Color.white;
            GameManager.instance.speedTimerIsRunning = false;
            GameManager.instance.speedMultiplierCountdown = 0f;
        }
    }
}
