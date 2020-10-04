# WebRCT Android Samples

![diagram](rtc_logo.png)

# What is WebRTC

WebRTC (Web Real-Time Communication) is a free, open-source project that provides web browsers and mobile applications with real-time communication (RTC) via simple application programming interfaces (APIs).

It allows audio and video communication to work inside web pages by allowing direct peer-to-peer communication, eliminating the need to install plugins or download native apps.

 Supported by *Apple*, *Google*, *Microsoft*, *Mozilla*, and *Opera*, WebRTC is being standardized through the World Wide Web Consortium (W3C) and the Internet Engineering Task Force (IETF).

Its mission is to "enable rich, high-quality RTC applications to be developed for the browser, mobile platforms, and IoT devices, and allow them all to communicate via a common set of protocols".

# This Project

The project demonstrates integrating WebRTC voice and video calls capability through Ant Media Android SDK.

# The Server Side

To run this sample you must install either **Ant Media CE Server** or the Enterprise Edition of the same server.
Ant Media Server is supported in **Ubuntu Linux 16.0.4+ and Mac OSX**.

You will also request Android studio and the latest Android SDK tools.

# WebRTC History

In May 2010, Google bought Global IP Solutions or GIPS, a VoIP and videoconferencing software company that had developed many components required for RTC, such as codecs and echo cancellation techniques. Google open-sourced the GIPS technology and engaged with relevant standards bodies at the IETF and W3C to ensure industry consensus.

In May 2011, Google released an open-source project for browser-based real-time communication known as WebRTC.This has been followed by ongoing work to standardize the relevant protocols in the IETF and browser APIs in the W3C.

In May 2011, **Ericsson Labs** built the first implementation of WebRTC using a modified WebKit library.In October 2011, the W3C published its first draft for the spec.WebRTC milestones include the first cross-browser video call (February 2013), first cross-browser data transfers (February 2014), and as of July 2014 Google Hangouts was "kind of" using WebRTC.

The W3C draft API was based on preliminary work done in the WHATWG. It was referred to as the ConnectionPeer API, and a pre-standards concept implementation was created at Ericsson Labs.The WebRTC Working Group expects this specification to evolve significantly based on:

Outcomes of ongoing exchanges in the companion RTCWEB group at IETF to define the set of protocols that, together with this document, define real-time communications in web browsers. While no one signaling protocol is mandated, SIP over WebSockets (RFC 7118) is often used partially due to the applicability of SIP to most of the envisaged communication scenarios as well as the availability of open-source software such as **JsSIP**.

Privacy issues that arise when exposing local capabilities and local streams
Technical discussions within the group, on implementing data channels in particular
Experience gained through early experimentation
Feedback from other groups and individuals
In November 2017, the WebRTC 1.0 specification transitioned from Working Draft to Candidate Recommendation

# Design

Major components of WebRTC include several **JavaScript APIs**:

- *getUserMedia* acquires the audio and video media (e.g., by accessing a device's camera and microphone).

- *RTCPeerConnection* enables audio and video communication between peers. It performs signal processing, codec handling, peer-to-peer communication, security, and bandwidth management.

- *RTCDataChannel* allows bidirectional communication of arbitrary data between peers. It uses the same API as WebSockets and has very low latency.

The WebRTC API also includes a statistics function:

- **getStats** allows the web application to retrieve a set of statistics about WebRTC sessions. These statistics data are being described in a separate W3C document.

The *WebRTC API* includes no provisions for **signaling**, that is discovering peers to connect to and determine how to establish connections among them. Applications use Interactive Connectivity Establishment for connections and somehow manage sessions, possibly relaying on any of Session Initiation Protocol, Extensible Messaging and Presence Protocol, Message Queuing Telemetry Transport, Matrix (protocol), or another protocol. Signaling may depend on one or more servers.

**RFC 7874** requires implementations to provide PCMA/PCMU (RFC 3551), Telephone Event as DTMF (RFC 4733), and Opus (RFC 6716) audio codecs as minimum capabilities. The PeerConnection, data channel and media capture browser APIs are detailed in the W3C.

W3C is developing **ORTC (Object Real-Time Communications)** for WebRTC.

# WebRTC Gateway

WebRTC Gateway connects between WebRTC and an established **VoIP** technology such as **SIP**. WebRTC (Web Real-Time Communication) is an API definition drafted by the World Wide Web Consortium (W3C) that supports browser-to-browser applications for voice calling, video chat, and messaging without the need of either internal or external plugins.