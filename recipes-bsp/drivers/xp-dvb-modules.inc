DESCRIPTION = "Hardware drivers for ${MACHINE}"
SECTION = "base"
PRIORITY = "required"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

KV = "4.10.12"

PV = "${KV}+${SRCDATE}"

SRC_URI = "http://source.mynonpublic.com/${MACHINE}/${MACHINE}-drivers-${KV}-${SRCDATE}.zip"

S = "${WORKDIR}"

INHIBIT_PACKAGE_STRIP = "1"

do_compile() {
}

do_populate_sysroot() {
}

FILES_${PN} += "${sysconfdir}/modules-load.d/_${MACHINE}.conf ${base_libdir}/modules/${KV}/extra"

do_compile() {
}

do_install() {
	install -d ${D}${base_libdir}/modules/${KV}/extra
	install -d ${D}/${sysconfdir}/modules-load.d
	for i in ${MACHINE}_1 ${MACHINE}_2 ${MACHINE}_3 ${MACHINE}_4; do
		install -m 0755 ${WORKDIR}/$i.ko ${D}${base_libdir}/modules/${KV}/extra
		echo $i >> ${D}/${sysconfdir}/modules-load.d/_${MACHINE}.conf
	done
}
