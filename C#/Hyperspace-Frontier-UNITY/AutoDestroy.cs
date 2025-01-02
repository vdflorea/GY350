using System.Collections;
using UnityEngine;

public class AutoDestroy : MonoBehaviour
{
    [SerializeField] private float lifetime = 2f;

    private void Awake() {
        StartCoroutine( ProcessLifetime());
    }
    private IEnumerator ProcessLifetime() {
        // Destroy a GameObject after a specified period of time
        yield return new WaitForSeconds(lifetime);
        Destroy(gameObject);
    }
}
