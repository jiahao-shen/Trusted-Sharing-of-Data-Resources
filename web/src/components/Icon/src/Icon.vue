<script setup lang="ts">
import type { IconInstance } from 'element-plus'
import { unref, ref, watch, nextTick } from 'vue'
import Iconify from '@purge-icons/generated'

const props = defineProps<{
	icon: string
	color: string
	size: number
}>()

const elRef = ref<IconInstance>()

const updateIcon = async (icon: string) => {
	const el = unref(elRef)
	if (!el) {
		return
	}

	await nextTick()
	if (!icon) {
		return
	}

	const svg = Iconify.renderSVG(icon, {})
	if (svg) {
		el.$el.textContent = ''
		el.$el.appendChild(svg)
	} else {
		const span = document.createElement('span')
		span.className = 'iconify'
		span.dataset.icon = icon
		el.$el.textContent = ''
		el.$el.appendChild(span)
	}
}

watch(
	() => props.icon,
	(value: string) => {
		updateIcon(value)
	}
)
</script>

<template>
	<div class="cursor-pointer flex justify-center items-center">
		<el-icon :size="size" :color="color" ref="elRef">
			<span class="iconify" :data-icon="icon"></span>
		</el-icon>
	</div>
</template>
