using UnityEngine;

public class Points_Debuff : Debuff
{
    protected override void ApplyDebuff()
    {
        // Divide score multiplier by 2 (half points)
        if (GameManager.instance.scoreMultiplier > 0.25)
        {
            GameManager.instance.scoreMultiplier /= 2;
        }
    }

    protected override void UpdateUI()
    {
        // Score is in a debuffed state
        if (GameManager.instance.scoreMultiplier < 1)
        {
            GameManager.instance.scoreText.color = Color.red;
            GameManager.instance.scoreTimerIsRunning = true;
            GameManager.instance.scoreMultiplierCountdown = 5f;
        }
        // Score multiplier in powered-up state initially; Now back to normal after collecting debuff
        else if (GameManager.instance.scoreMultiplier == 1f)
        {
            GameManager.instance.scoreText.color = Color.white;
            GameManager.instance.scoreTimerIsRunning = false;
            GameManager.instance.scoreMultiplierCountdown = 0f;
        }
    }
}
