# Dirac for Android

Dirac is an experimental Android VPN client derived from [v2rayNG](https://github.com/2dust/v2rayNG).

The project keeps the mature Android VPN/service architecture of v2rayNG while developing Dirac-specific transport and censorship-resilience work around Xray.

> Status: active development. There is no production-ready public release yet.

## Upstream

Dirac for Android is based on the open-source [2dust/v2rayNG](https://github.com/2dust/v2rayNG) project and retains substantial upstream code and project structure.

The repository is a GitHub fork of v2rayNG. Upstream copyright and license notices remain applicable to the inherited code.

The desktop sibling is [Dirac-Desktop](https://github.com/Dimsho03/Dirac-Desktop), which is derived from v2rayN.

## Current Dirac work

The current Android work includes:

- Xray-based VPN operation inherited from v2rayNG;
- Dirac-specific ECH interoperability work;
- a stored Xray direct-path TLS-record fragmentation patch for the ECH path;
- ongoing work on a manual direct anti-DPI reserve mode;
- separation between application code, native-core patches, and private runtime configuration.

The tested ECH fix was developed on a v2rayNG 2.3.8 baseline. Newer upstream revisions on master must be revalidated before they are treated as a Dirac release.

See [docs/ECH_FIX.md](docs/ECH_FIX.md) for the ECH investigation and [patches/xray-core/ech-direct-path-fragmentation.patch](patches/xray-core/ech-direct-path-fragmentation.patch) for the stored Xray patch.

## Repository layout

- V2rayNG/ — Android application inherited from v2rayNG.
- AndroidLibXrayLite — upstream native Xray Android wrapper submodule.
- hev-socks5-tunnel — upstream tunnel submodule.
- patches/xray-core/ — Dirac patches maintained outside the upstream submodule.
- docs/ — Dirac-specific technical notes.
- compile-hevtun.sh — native tunnel build helper inherited from the upstream project.

## Building

Requirements are the same general Android toolchain family used by upstream v2rayNG: Android SDK/NDK, Java, Git submodules, and the Gradle wrapper under V2rayNG/.

Clone with submodules:

    git clone --recurse-submodules https://github.com/Dimsho03/Dirac.git
    cd Dirac

The Android project lives under V2rayNG/.

A stock upstream libv2ray.aar is **not** equivalent to a tested Dirac runtime. A Dirac build that is intended to reproduce the ECH fix must use an Xray build produced from the documented Dirac patch and the matching wrapper revision.

No automated public release workflow is enabled yet. This is intentional until the custom Xray build is reproducible and pinned end-to-end in CI.

## Security and issue reports

Never commit or publish real:

- VPN profiles or subscription URLs;
- UUIDs, passwords, access tokens, cookies, or private keys;
- exported MMKV/application state;
- user logs containing private endpoint data;
- APK/AAR build outputs or local signing material.

Redact sensitive endpoint information before posting logs or issues.

## License

Dirac for Android is distributed under the [GNU General Public License v3.0](LICENSE), consistent with the v2rayNG-derived codebase.

## Acknowledgements

- [v2rayNG](https://github.com/2dust/v2rayNG) — upstream Android client and architecture.
- [Xray-core](https://github.com/XTLS/Xray-core) — proxy core used by Dirac.
- [AndroidLibXrayLite](https://github.com/2dust/AndroidLibXrayLite) — Android wrapper used by the upstream project.
- [hev-socks5-tunnel](https://github.com/heiher/hev-socks5-tunnel) — tunnel component used by the Android client.