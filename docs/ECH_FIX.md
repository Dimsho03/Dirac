# Dirac ECH direct-path fix

## Scope

Dirac is based on v2rayNG 2.3.8 at commit 200c59f77332b4732d3261fc91701b415dab7464 with AndroidLibXrayLite v26.9.9. The Xray-core dependency used for the fix is commit 52a412d9e2f5c2a5142b1b4e2ab3771dacb8b120.

The observed failure was specific to ECH on the direct local network path. A no-ECH profile worked. A raw Go TLS 1.3 plus ECH probe timed out on the direct path on both Windows and Android, but the identical probe succeeded with ECHAccepted=true from the VPS and from the same Android device when carried inside the working no-ECH VPN. This isolated the problem from v2rayNG, Xray, Android, Go, the ECHConfig, and Cloudflare.

## Fix

patches/xray-core/ech-direct-path-fragmentation.patch applies three changes to the exact Xray-core baseline above:

1. ECH forces TLS 1.3-compatible MinVersion and MaxVersion.
2. The uTLS config copy preserves MinVersion and MaxVersion.
3. Connections with an ECHConfigList wrap the first ClientHello write and split the ClientHello across two TLS records inside the outer SNI hostname. TLS handshake messages may span record boundaries, so the peer still reconstructs the same ClientHello while a record-local on-path parser no longer sees the intact first ClientHello in one record.

The wrapper is used by both the native TLS Client and uTLS UClient paths.

## Verification

Focused tests pass:

- TestECHFragmentConnSplitsSNIAndPreservesHandshake
- TestECHFragmentConnPassesNonClientHello
- TestApplyECHForcesTLS13
- TestCopyConfigPreservesTLSVersionsForECH

Device A/B after rebuilding AndroidLibXrayLite and Dirac:

- POLAND-edge-no-ECH: TCP probe exit 0 and VLESS tunneling.
- POLAND-edge-ECH: TCP probe exit 0, ECHConfig fetched, WebSocket established, and VLESS tunneling.

The tested fragmentation AAR SHA-256 is 06073a2dfcfdeb78333de8e86959d72697a1526b85fd05160134f17ea79106b0.

The tested armeabi-v7a APK SHA-256 is f9ed7f630431eedaf503082634374eb8f88f5ed837e40630a1d8e7a0cb0c26b0.

## Rebuild outline

1. Check out Xray-core at 52a412d9e2f5c2a5142b1b4e2ab3771dacb8b120.
2. Apply patches/xray-core/ech-direct-path-fragmentation.patch.
3. In an AndroidLibXrayLite v26.9.9 build tree, point github.com/xtls/xray-core to that local patched tree with a temporary Go module replace.
4. Build with Go 1.27.1 and the matching gomobile toolchain using:
   gomobile bind -androidapi 24 -trimpath "-ldflags=-s -w -buildid= -checklinkname=0" ./
5. Stage the produced libv2ray.aar as V2rayNG/app/libs/libv2ray.aar.
6. Build :app:assemblePlaystoreDebug with Gradle 9.5.1.

The AAR is a build artifact and is intentionally not committed.