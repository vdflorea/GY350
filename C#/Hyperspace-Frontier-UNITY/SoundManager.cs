using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class SoundManager : MonoBehaviour
{
    public static SoundManager instance;
    
    [SerializeField] private AudioSource audioSource;
    [SerializeField] private AudioClip debuffSound;
    [SerializeField] private AudioClip explodeSound;
    [SerializeField] private AudioClip hyperspaceSound;
    [SerializeField] private AudioClip menuMusic;
    [SerializeField] private AudioClip powerUpSound;
    [SerializeField] private AudioClip railGunSound;
    [SerializeField] private AudioClip takeOffSound;
    
    [SerializeField] private Button soundToggleButton;
    [SerializeField] private TMP_Text buttonText;

    private bool _isSoundOn = true;
    
    private void Awake() 
    {
        instance = this;
    }
    
    private void Start()
    {
        // Initialize the button state
        UpdateButtonState();
    }

    public void ToggleSound()
    {
        _isSoundOn = !_isSoundOn;
        UpdateButtonState();
        ApplySoundSettings();
    }

    private void UpdateButtonState()
    {
        // Update colour of button (on/off : green/red)
        ColorBlock colors = soundToggleButton.colors;
        colors.normalColor = _isSoundOn ? Color.green : Color.red;
        colors.selectedColor = _isSoundOn ? Color.green : Color.red;
        colors.highlightedColor = _isSoundOn ? Color.green : Color.red;
        soundToggleButton.colors = colors;

        // Update text (ON/OFF)
        buttonText.text = _isSoundOn ? "Sounds: ON" : "Sounds: OFF";
    }

    private void ApplySoundSettings()
    {
        // Mute/unmute audio
        AudioListener.volume = _isSoundOn ? 1f : 0f;
        
        // Save the preference so that when scene is reloaded, state is saved
        PlayerPrefs.SetInt("SoundOn", _isSoundOn ? 1 : 0);
        PlayerPrefs.Save();
    }
    
    public void LoadSoundSettings()
    {
        // Load the player's sound settings
        _isSoundOn = PlayerPrefs.GetInt("SoundOn", 1) == 1;
        UpdateButtonState();
        ApplySoundSettings();
    }

    public void PlayDebuffSound()
    {
        audioSource.PlayOneShot(debuffSound);
    }

    public void PlayExplodeSound()
    {
        audioSource.PlayOneShot(explodeSound);
    }

    public void PlayHyperspaceSound()
    {
        // Loop the Hyperspace sound effect
        audioSource.clip = hyperspaceSound;
        audioSource.loop = true;
        audioSource.Play();
    }

    public void StopHyperspaceSound()
    {
        audioSource.Stop();
    }

    public void PlayMenuMusic()
    {
        // Loop the menu music
        audioSource.clip = menuMusic;
        audioSource.loop = true;
        audioSource.Play();
    }
    
    public void StopMenuMusic()
    {
        audioSource.Stop();
    }
    
    public void PlayPowerUpSound()
    {
        audioSource.PlayOneShot(powerUpSound);
    }

    public void PlayRailgunSound()
    {
        audioSource.PlayOneShot(railGunSound);
    }

    public void PlayTakeOffSound()
    {
        audioSource.PlayOneShot(takeOffSound);
    }
}
