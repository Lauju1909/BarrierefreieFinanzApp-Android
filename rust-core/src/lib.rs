use jni::objects::{JClass, JString};
use jni::sys::{jboolean, jstring, JNI_FALSE, JNI_TRUE};
use jni::JNIEnv;
use sha2::{Digest, Sha256};

pub fn compute_string_sha256(input: &str) -> String {
    let mut hasher = Sha256::new();
    hasher.update(input.as_bytes());
    hex::encode(hasher.finalize())
}

pub fn sanitize_json_or_text(input: &str) -> String {
    input
        .chars()
        .filter(|&c| c != '\0')
        .collect::<String>()
        .trim()
        .to_string()
}

#[no_mangle]
pub extern "system" fn Java_de_lauri_finanzapp_RustAppCore_sha256Hex<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    input: JString<'local>,
) -> jstring {
    if input.is_null() {
        return std::ptr::null_mut();
    }
    let s: String = match env.get_string(&input) {
        Ok(v) => v.into(),
        Err(_) => return std::ptr::null_mut(),
    };
    let digest = compute_string_sha256(&s);
    match env.new_string(digest) {
        Ok(jstr) => jstr.into_raw(),
        Err(_) => std::ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_de_lauri_finanzapp_RustAppCore_verifyIntegrity<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    payload: JString<'local>,
    expected_sha256: JString<'local>,
) -> jboolean {
    if payload.is_null() || expected_sha256.is_null() {
        return JNI_FALSE;
    }
    let data: String = match env.get_string(&payload) {
        Ok(v) => v.into(),
        Err(_) => return JNI_FALSE,
    };
    let expected: String = match env.get_string(&expected_sha256) {
        Ok(v) => v.into(),
        Err(_) => return JNI_FALSE,
    };
    let actual = compute_string_sha256(&data);
    if actual.eq_ignore_ascii_case(expected.trim()) {
        JNI_TRUE
    } else {
        JNI_FALSE
    }
}

#[no_mangle]
pub extern "system" fn Java_de_lauri_finanzapp_RustAppCore_sanitizeInput<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    raw: JString<'local>,
) -> jstring {
    if raw.is_null() {
        return std::ptr::null_mut();
    }
    let s: String = match env.get_string(&raw) {
        Ok(v) => v.into(),
        Err(_) => return std::ptr::null_mut(),
    };
    let clean = sanitize_json_or_text(&s);
    match env.new_string(clean) {
        Ok(jstr) => jstr.into_raw(),
        Err(_) => std::ptr::null_mut(),
    }
}


#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_sha256_computation_and_determinism() {
        let h1 = compute_string_sha256("test_payload_123");
        let h2 = compute_string_sha256("test_payload_123");
        let h3 = compute_string_sha256("other_payload_456");
        assert_eq!(h1.len(), 64);
        assert_eq!(h1, h2);
        assert_ne!(h1, h3);
    }

    #[test]
    fn test_sanitize_json_or_text() {
        let dirty = "  hello\0world  ";
        assert_eq!(sanitize_json_or_text(dirty), "helloworld");
    }
}
