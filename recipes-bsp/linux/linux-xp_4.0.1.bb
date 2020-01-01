DESCRIPTION = "Linux kernel for ${MACHINE}"
SECTION = "kernel"
LICENSE = "GPLv2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

KERNEL_RELEASE = "4.0.1"

COMPATIBLE_MACHINE = "^(xp1000)$"

SRC_URI[md5sum] = "c274792d088cd7bbfe7fe5a76bd798d8"
SRC_URI[sha256sum] = "6fd63aedd69b3b3b28554cabf71a9efcf05f10758db3d5b99cfb0580e3cde24c"

LIC_FILES_CHKSUM = "file://${WORKDIR}/linux-${PV}/COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

# By default, kernel.bbclass modifies package names to allow multiple kernels
# to be installed in parallel. We revert this change and rprovide the versioned
# package names instead, to allow only one kernel to be installed.
PKG_kernel-base = "kernel-base"
PKG_kernel-image = "kernel-image"
RPROVIDES_${KERNEL_PACKAGE_NAME}-base = "kernel-${KERNEL_VERSION}"
RPROVIDES_${KERNEL_PACKAGE_NAME}-image = "kernel-image-${KERNEL_VERSION}"

SRC_URI += "http://downloads.mutant-digital.net/linux-${PV}.tar.gz \
	file://defconfig \
	file://0001-bcmgenet.patch \
	file://add-dmx-source-timecode.patch \
	file://iosched-slice_idle-1.patch \
	file://improve_the_overall_abi_and_fpu_mode_checks.patch \
	file://set_o32_default_fpu_flags.patch \
	file://0001-Support-TBS-USB-drivers-for-4.0.1-kernel.patch \
	file://0001-TBS-fixes-for-4.0.1-kernel.patch \
	file://0001-STV-Add-PLS-support.patch \
	file://0001-STV-Add-SNR-Signal-report-parameters.patch \
	file://blindscan2.patch \
	file://0001-stv090x-optimized-TS-sync-control.patch \
        file://0002-log2-give-up-on-gcc-constant-optimizations.patch \
        file://0003-makefile-disable-warnings.patch \
        file://0004-cp1emu-do-not-use-bools-for-arithmetic.patch \
	"

inherit kernel machine_kernel_pr

S = "${WORKDIR}/linux-${PV}"

export OS = "Linux"
KERNEL_OBJECT_SUFFIX = "ko"
KERNEL_IMAGETYPE = "vmlinux.gz"
KERNEL_OUTPUT = "vmlinux.gz"
KERNEL_OUTPUT_DIR = "."
KERNEL_IMAGEDEST = "tmp"

FILES_${KERNEL_PACKAGE_NAME}-image = "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}"

KERNEL_EXTRA_ARGS = "EXTRA_CFLAGS=-Wno-attribute-alias"

kernel_do_install_append() {
        install -d ${D}/${KERNEL_IMAGEDEST}
        install -m 0755 ${KERNEL_OUTPUT} ${D}/${KERNEL_IMAGEDEST}
}

pkg_postinst_kernel-image () {
	if [ "x$D" == "x" ]; then
		if [ -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} ] ; then
			flash_eraseall /dev/mtd1
			nandwrite -p /dev/mtd1 /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}
		fi
	fi
	true
}
